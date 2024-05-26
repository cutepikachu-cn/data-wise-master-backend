package cn.cutepikachu.datawisemaster.mq;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import cn.cutepikachu.datawisemaster.manager.AiManager;
import cn.cutepikachu.datawisemaster.model.entity.Chart;
import cn.cutepikachu.datawisemaster.model.enums.GenStatus;
import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import cn.cutepikachu.datawisemaster.service.IChartService;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.hutool.core.util.StrUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 消费者
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-26 18:11:00
 */
@Component
@Slf4j
public class MessageConsumer {
    @Resource
    private IChartService chartService;
    @Resource
    private AiManager aiManager;

    @RabbitListener(queues = {MQConstant.ANALYSIS_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Receive analysis message = {}", message);
            if (StrUtil.isBlank(message)) {
                // 如果失败，消息拒绝
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ResponseCode.SYSTEM_ERROR, "消息为空");
            }

            // 获取图表信息
            long chartId = Long.parseLong(message);
            Chart chart = chartService.getById(chartId);
            if (chart == null) {
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ResponseCode.NOT_FOUND_ERROR, "未找到图标");
            }

            // 设置分析中状态
            chart.setGenStatus(GenStatus.RUNNING);
            if (!chartService.updateById(chart)) {
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ResponseCode.OPERATION_ERROR);
            }
            channel.basicNack(deliveryTag, false, false);

            // AI 调用
            String userInput = getUserInput(chart);
            String aiGenResult = aiManager.doChat(userInput);
            List<String> splitResult = StrUtil.split(aiGenResult, "*****");
            ThrowUtil.throwIf(splitResult.size() < 2, ResponseCode.OPERATION_ERROR, "生成错误");
            String genChart = splitResult.get(1).trim();
            String genResult = splitResult.get(2).trim();
            chart.setGenChart(genChart)
                    .setGenResult(genResult)
                    // 设置分析完成状态
                    .setGenStatus(GenStatus.SUCCEED);

            if (!chartService.updateById(chart)) {
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ResponseCode.OPERATION_ERROR);
            }

            channel.basicAck(deliveryTag, false);
            log.info("Success analysis message = {}", message);
        } catch (Exception e) {
            log.error("Error analysis message: {}", message, e);
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (Exception nackEx) {
                log.error("Error sending analysis message to DLX: {}", message, nackEx);
            }
        }
    }

    private String getUserInput(Chart chart) {
        // 拼接用户输入
        ChartVO chartVO = chartService.getChartVO(chart);
        String goal = chartVO.getGoal();
        String name = chartVO.getName();
        String chartType = chartVO.getChartType();
        List<Map<String, String>> data = chartVO.getData();

        String userInput = "分析需求: \n" + goal + '\n' +
                "图表名称为" + name + '\n' +
                "图表类型为" + chartType + '\n' +
                "原始数据: \n" + data;

        return userInput;
    }
}

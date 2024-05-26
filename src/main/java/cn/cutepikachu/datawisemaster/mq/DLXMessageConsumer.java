package cn.cutepikachu.datawisemaster.mq;

import cn.cutepikachu.datawisemaster.model.entity.Chart;
import cn.cutepikachu.datawisemaster.model.enums.GenStatus;
import cn.cutepikachu.datawisemaster.service.IChartService;
import cn.hutool.core.util.StrUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 死信队列消费者
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-26 19:14:00
 */
@Component
@Slf4j
public class DLXMessageConsumer {
    @Resource
    private IChartService chartService;

    @RabbitListener(queues = {MQConstant.ANALYSIS_DLX_QUEUE_NAME}, ackMode = "AUTO")
    public void receiveDLXMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.error("Received DLX analysis message = {}", message);
            if (StrUtil.isBlank(message)) {
                channel.basicAck(deliveryTag, false);
                log.error("DLX analysis message is blank");
                return;
            }
            long chartId = Long.parseLong(message);
            Chart chart = chartService.getById(chartId);
            if (chart == null) {
                channel.basicAck(deliveryTag, false);
                log.error("Chart not found for ID: {}", chartId);
                return;
            }
            // 设置生成错误状态
            chart.setGenStatus(GenStatus.ERROR);
            if (!chartService.updateById(chart)) {
                log.error("Failed to update chart status to ERROR for ID: {}", chartId);
                channel.basicNack(deliveryTag, false, false);
                return;
            }
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Exception while processing DLX analysis message: {}", message, e);
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (Exception nackEx) {
                log.error("Exception while sending NACK for DLX analysis message: {}", message, nackEx);
            }
        }
    }
}

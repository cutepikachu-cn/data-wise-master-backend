package cn.cutepikachu.datawisemaster.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建分析处理队列
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-26 18:55:00
 */
public class QueueInit {

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // 声明主交换机
            channel.exchangeDeclare(MQConstant.ANALYSIS_EXCHANGE_NAME, "direct");
            // 声明死信交换机
            channel.exchangeDeclare(MQConstant.ANALYSIS_DLX_EXCHANGE_NAME, "direct");

            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", MQConstant.ANALYSIS_DLX_EXCHANGE_NAME);
            arguments.put("x-dead-letter-routing-key", MQConstant.ANALYSIS_DLX_ROUTING_KEY);

            // 主队列
            String queueName = MQConstant.ANALYSIS_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, arguments);
            channel.queueBind(queueName, MQConstant.ANALYSIS_EXCHANGE_NAME, MQConstant.ANALYSIS_ROUTING_KEY);

            // 私信队列
            String dlxQueueName = MQConstant.ANALYSIS_DLX_QUEUE_NAME;
            channel.queueDeclare(dlxQueueName, true, false, false, arguments);
            channel.queueBind(dlxQueueName, MQConstant.ANALYSIS_DLX_EXCHANGE_NAME, MQConstant.ANALYSIS_DLX_ROUTING_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

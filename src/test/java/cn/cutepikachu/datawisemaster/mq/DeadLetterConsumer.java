package cn.cutepikachu.datawisemaster.mq;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-22 15:28:00
 */
public class DeadLetterConsumer {
    private final static String QUEUE_NAME = "queue_dlx_demo";
    // 死信队列
    private final static String DLX_QUEUE_NAME = "dlx_queue";

    public static void main(String[] argv) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 为主队列创建消费者
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");

            if (RandomUtil.randomBoolean()) {
                // 确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } else {
                // 拒绝消息并将其重新排队到 DLX
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });

        // 为死信队列创建消费者
        Channel dlxChannel = connection.createChannel();
        DeliverCallback dlxDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [DLX] Received '" + message + "'");
        };
        dlxChannel.basicConsume(DLX_QUEUE_NAME, true, dlxDeliverCallback, consumerTag -> {
        });
    }
}

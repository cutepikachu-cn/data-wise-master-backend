package cn.cutepikachu.datawisemaster.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-22 15:20:00
 */
public class TtlProducer {
    private final static String QUEUE_NAME = "ttl_queue";

    public static void main(String[] argv) throws Exception {
        // 创建连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // factory.setUsername("guest");
        // factory.setPassword("guest");
        // 创建连接
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 创建队列
            Map<String, Object> args = new HashMap<>();
            // 队列过期时间设置
            args.put("x-message-ttl", 60000);
            channel.queueDeclare(QUEUE_NAME, false, false, false, args);
            String message = "Hello World!";

            // 消息过期时间配置
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .expiration("10000")
                    .build();

            channel.basicPublish("", QUEUE_NAME, properties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}

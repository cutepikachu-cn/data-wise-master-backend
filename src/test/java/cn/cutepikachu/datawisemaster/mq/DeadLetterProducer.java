package cn.cutepikachu.datawisemaster.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-22 15:20:00
 */
public class DeadLetterProducer {
    private final static String EXCHANGE_NAME = "exchange_dlx_demo";
    private final static String ROUTING_KEY = "routing_key_dlx";
    private final static String QUEUE_NAME = "queue_dlx_demo";
    // 私信队列
    private final static String DLX_EXCHANGE_NAME = "dlx_exchange";
    // 死信交换机
    private final static String DLX_QUEUE_NAME = "dlx_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明死信交换机
            channel.exchangeDeclare(DLX_EXCHANGE_NAME, "direct");

            // 声明死信队列
            channel.queueDeclare(DLX_QUEUE_NAME, true, false, false, null);
            channel.queueBind(DLX_QUEUE_NAME, DLX_EXCHANGE_NAME, ROUTING_KEY);

            // 声明主交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            Map<String, Object> args = new HashMap<>();
            // 绑定死信交换机及死信路由
            args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
            args.put("x-dead-letter-routing-key", ROUTING_KEY);
            // 声明主交换机
            channel.queueDeclare(QUEUE_NAME, true, false, false, args);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNext()) {
                String message = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
            }

        }
    }
}

package cn.cutepikachu.datawisemaster.mq;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class TopicConsumer {

    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Channel channel = connection.createChannel();

                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

                String queueName = "queue-topic-exchange:" + i + ":" + j;
                channel.queueDeclare(queueName, true, false, false, null);
                // channel.queueBind(queueName, EXCHANGE_NAME, "route." + i + ".*");
                channel.queueBind(queueName, EXCHANGE_NAME, "route." + i + ".#");
                System.out.println(" [" + i + ":" + j + "] Waiting for messages. To exit press CTRL+C");

                int finalI = i;
                int finalJ = j;
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [" + finalI + ":" + finalJ + "] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            }
        }
    }
}

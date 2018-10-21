package cn.zyblogs.rabbitmq.api.exchange.topic;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Consumer4TopicExchange.java
 * @Package cn.zyblogs.rabbitmq.api.exchange.topic
 * @Description: TODO
 * @Author ZhangYB
 * @Version V1.0
 */
@Slf4j
public class Consumer4TopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory() ;

        connectionFactory.setHost("192.168.32.143");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 是否支持自动重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();
        log.info("==========Consumer连接成功==========");
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        // *匹配一个词
        String routingKey = "user.*";
        // #匹配多个词
//        String routingKey = "user.#";

        // 声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 建立交换机和队列的绑定关系:
        channel.queueBind(queueName, exchangeName, routingKey);

// 设置channel  true 是否自动接收   new DefaultConsumer 消费者
        channel.basicConsume(queueName, true, "myConsumerTag" , new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("消费端:" + new String(body));
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag, false);
            }
        });

    }
}

package cn.zyblogs.rabbitmq.api.consumer;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Consumer.java
 * @Package cn.zyblogs.rabbitmq.api.returnlistener
 * @Description: TODO
 * @Author ZhangYB
 * @Version V1.0
 */
@Slf4j
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMqServer.IP);
        connectionFactory.setPort(RabbitMqServer.PORT);
        connectionFactory.setVirtualHost(RabbitMqServer.VIRTUAL_HOST);
        // 获取连接
        final Connection connection = connectionFactory.newConnection();
        log.info("============Consumer连接成功==============");

        // 通过connection创建一个channel
        final Channel channel = connection.createChannel();

        String exchangeName = "test_consumer_exchange";
        String routingKey = "consumer.#";
        String queueName = "test_consumer_queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        channel.queueDeclare(queueName, true, false, false,null );

        // 绑定
        channel.queueBind(queueName, exchangeName,routingKey );

        // 消费者
        // 设置channel  autoACK true 自动接收   new DefaultConsumer 消费者
        channel.basicConsume(queueName, true , new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("消费端:" +new String(body));
                System.err.println("consumerTag:" + consumerTag);
                System.err.println("envelope:" + envelope);
                System.err.println("properties:" + properties);
                long deliveryTag = envelope.getDeliveryTag();
                System.err.println("deliveryTag:" +deliveryTag);
                channel.basicAck(deliveryTag, false);
            }
        });
    }
}

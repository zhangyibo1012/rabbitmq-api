package cn.zyblogs.rabbitmq.api.ack;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Consumer.java
 * @Package cn.zyblogs.rabbitmq.api.confirm
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

        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.#";
        String queueName = "test_ack_queue";

        // 声明exchange和queue
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);

        // 绑定 指定路由key
        channel.queueBind(queueName, exchangeName, routingKey);


        // 设置channel  false手动接收   new DefaultConsumer 消费者
        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("消费端:" + new String(body));
                long deliveryTag = envelope.getDeliveryTag();
                final int num = (int) properties.getHeaders().get("num");
                if (num == 1) {
                    // 拒绝 requeue==true 重回队列尾部
                    channel.basicNack(deliveryTag, false, true);
                } else {
                    // 手工签收 false不批量签收 ack消费成功
                    channel.basicAck(deliveryTag, false);
                }
            }
        });

    }
}

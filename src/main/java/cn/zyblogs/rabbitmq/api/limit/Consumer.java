package cn.zyblogs.rabbitmq.api.limit;

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

        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.#";
        String queueName = "test_qos_queue";

        // 声明exchange和queue
        channel.exchangeDeclare(exchangeName, "topic", true ,false,null);
        channel.queueDeclare(queueName, true, false, false, null);

        // 绑定 指定路由key
        channel.queueBind(queueName, exchangeName, routingKey);

        // 限流 autoAck 设为false  手动签收 1次一条
        channel.basicQos(0,1,false );

        // 设置channel  false 手动接收   new DefaultConsumer 消费者
        channel.basicConsume(queueName, false , new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("消费端:" +new String(body));
                long deliveryTag = envelope.getDeliveryTag();
                // 手工签收 false不批量签收
                channel.basicAck(deliveryTag, false);
            }
        });

    }
}

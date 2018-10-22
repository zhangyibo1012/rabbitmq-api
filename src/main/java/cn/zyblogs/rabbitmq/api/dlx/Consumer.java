package cn.zyblogs.rabbitmq.api.dlx;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        // 这是一个普通的交换机队列和路由key
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";

        // 声明exchange和queue
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        // args参数设置到队列上
        Map<String ,Object> arguments = new HashMap<>(2);
        // 死信队列属性 路由失败重发到只当的死信对列
        arguments.put("x-dead-letter-exchange", "dlx.exchange");
        channel.queueDeclare(queueName, true, false, false, arguments);

        // 绑定 指定路由key
        channel.queueBind(queueName, exchangeName, routingKey);

        //要进行死信队列的声明:
        channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
        channel.queueDeclare("dlx.queue", true, false, false, null);
        // # 所有的都会路由
        channel.queueBind("dlx.queue", "dlx.exchange", "#");


        // 设置channel  false手动接收   new DefaultConsumer 消费者
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("-----------consume message----------");
                System.err.println("consumerTag: " + consumerTag);
                System.err.println("envelope: " + envelope);
                System.err.println("properties: " + properties);
                System.err.println("body: " + new String(body));
            }
        });

    }
}

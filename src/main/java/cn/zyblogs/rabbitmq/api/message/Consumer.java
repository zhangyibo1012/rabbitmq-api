package cn.zyblogs.rabbitmq.api.message;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Consumer.java
 * @Package cn.zyblogs.rabbitmq.quickstart
 * @Description: TODO
 * @Author ZhangYB
 * @Version V1.0
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个ConnectionFactory进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.32.144");
        connectionFactory.setPort(5672);
        // 设置虚拟主机
        connectionFactory.setVirtualHost("/");

        // 通过连接工厂获得连接
        final Connection connection = connectionFactory.newConnection();
        System.out.println("========Consumer连接成功============");
        // 通过connection创建一个Channel
        final Channel channel = connection.createChannel();

        // 声明一个队列
        String queueName = "test002";
        channel.queueDeclare(queueName, true, false,false ,null );

        // 创建消费者 废弃
//        final DefaultConsumer defaultConsumer = new DefaultConsumer(channel);

        // 设置channel  true 是否自动接收   new DefaultConsumer 消费者
        channel.basicConsume(queueName, true, "myConsumerTag" , new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println(new String(body));
                final Map<String, Object> headers = properties.getHeaders();
                headers.forEach((k,v)-> log.info("key: " + k + "value:" + v));
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag, false);
            }
        });

    }

}

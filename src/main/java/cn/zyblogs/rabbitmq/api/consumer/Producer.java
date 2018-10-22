package cn.zyblogs.rabbitmq.api.consumer;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Title: Producer.java
 * @Package cn.zyblogs.rabbitmq.api.returnlistener
 * @Description: TODO
 * @Author ZhangYB
 * @Version V1.0
 */
@Slf4j
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMqServer.IP);
        connectionFactory.setPort(RabbitMqServer.PORT);
        connectionFactory.setVirtualHost(RabbitMqServer.VIRTUAL_HOST);
        // 获取连接
        final Connection connection = connectionFactory.newConnection();
        log.info("============Producer连接成功==============");

        // 通过connection创建一个channel
        final Channel channel = connection.createChannel();

        String exchange = "test_consumer_exchange";
        String routingKey = "consumer.save";
        String msg = "Hello RabbitMQ Consumer Message";



        // mandatory 如果是true 则监听器会接收到路由不可达的消息，然后进行后续处理，如果false，那么broker端会自动删除该消息
//        channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

//        channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());

        IntStream.rangeClosed(1, 5).forEach((c)->{
                    try {
                        channel.basicPublish(exchange,routingKey,true,null  , msg.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}

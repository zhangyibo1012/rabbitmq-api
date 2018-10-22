package cn.zyblogs.rabbitmq.api.returnlistener;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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

        String exchange = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";
        String msg = "Hello RabbitMQ Return Message";

        // 监控不可达的消息 后续处理
        channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {
            System.err.println("----------handleReturn-------------");
            System.err.println("replyCode:" + replyCode);
            System.err.println("replyText:" + replyText);
            System.err.println("exchange:" + exchange1);
            System.err.println("routingKey:" + routingKey1);
            System.err.println("properties:" + properties);
            System.err.println("body:" + new String(body));
        });

        // mandatory 如果是true 则监听器会接收到路由不可达的消息，然后进行后续处理，如果false，那么broker端会自动删除该消息
        channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

//        channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
    }
}

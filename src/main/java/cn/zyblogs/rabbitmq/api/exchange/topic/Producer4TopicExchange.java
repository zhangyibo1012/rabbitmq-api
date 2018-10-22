package cn.zyblogs.rabbitmq.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Producer4TopicExchange.java
 * @Package cn.zyblogs.rabbitmq.api.exchange.topic
 * @Description: TODO 所有发送到Topic Exchange 的消息被转发都所有关心RouteKey中指定的Topic的Queue上
 *                  Exchange将RouteKey和某Topic进行模糊匹配，此时队列需要绑定一个Topic ，可以使用通配符匹配 #匹配一个或多个词  *匹配一个词
 *                          log.# 可匹配到log.info.oa
 *                          log.* 只可匹配log.error
 * @Author ZhangYB
 * @Version V1.0
 */
@Slf4j
public class Producer4TopicExchange {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建一个ConnectionFactory进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.32.144");
        connectionFactory.setPort(5672);
        // 设置虚拟主机
        connectionFactory.setVirtualHost("/");

        // 通过连接工厂获得连接
        final Connection connection = connectionFactory.newConnection();
        log.info("==========Consumer连接成功==========");
        // 通过connection创建一个Channel
        final Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.save";
        String routingKey2 = "user.update";
        String routingKey3 = "user.delete.abc";

        // 发送
        String msg = "Hello World RabbitMQ 4  Direct Exchange Message  ... ";
        channel.basicPublish(exchangeName, routingKey1 , null , msg.getBytes());
        channel.basicPublish(exchangeName, routingKey2 , null , msg.getBytes());
        channel.basicPublish(exchangeName, routingKey3 , null , msg.getBytes());
        channel.close();
        connection.close();
    }
}

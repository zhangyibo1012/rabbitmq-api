package cn.zyblogs.rabbitmq.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Producer4DirectExchange.java
 * @Package cn.zyblogs.rabbitmq.api.exhange.direct
 * @Description: TODO  Exchange交换机 :接收消息并根据路由键转发消息到所绑定的队列
 *                     Direct Exchange 所有发送到Direct Exchange的消息被转发到RouteKey中指定的Queue。  routeKey必须一致,完全匹配
 *
 *                    Direct模式可以使用RabbitMQ自带的Exchange:default exchange所以不需要将Exchange进行任何绑定操作，消息传递时，RouteKey必须完全匹配才会被队列接收，否则该消息会被抛弃
 * @Author ZhangYB
 * @Version V1.0
 */
@Slf4j
public class Producer4DirectExchange {
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
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";

        // 发送
        String msg = "Hello World RabbitMQ 4  Direct Exchange Message  ... ";
        channel.basicPublish(exchangeName, routingKey , null , msg.getBytes());
    }
}

package cn.zyblogs.rabbitmq.api.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Title: Producer4FanoutExchange.java
 * @Package cn.zyblogs.rabbitmq.api.exchange.fanout
 * @Description: TODO  不处理路由键 只需要简单的将队列绑定到交换机上
 *                     发送到交换机的消息都会被转发到与该交换机绑定的所有队列上
 *                     Fanout交换机转发消息最快 不会走路由
 *
 * @Author ZhangYB
 * @Version V1.0
 */
@Slf4j
public class Producer4FanoutExchange {
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
        String exchangeName = "test_fanout_exchange";

        // 发送10次数据
        IntStream.rangeClosed(1,10 ).forEach((c) -> {
            String msg = "Hello World RabbitMQ 4  Direct Exchange Message  ... ";
            try {
                channel.basicPublish(exchangeName, "zz" , null , msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        channel.close();
        connection.close();
    }
}

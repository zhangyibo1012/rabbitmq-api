package cn.zyblogs.rabbitmq.api.message;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Title: Producer.java
 * @Package cn.zyblogs.rabbitmq.quickstart
 * @Description: TODO Virtual host 虚拟主机 虚拟地址 用于进行逻辑隔离 最上层的消息路由
 *          一个Virtual host里可以有若干个exchange和queue
 *          同一个Virtual host不能有相同名称的exchange和queue
 * @Author ZhangYB
 * @Version V1.0
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建一个ConnectionFactory进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMqServer.IP);
        connectionFactory.setPort(RabbitMqServer.PORT);
        // 设置虚拟主机
        connectionFactory.setVirtualHost(RabbitMqServer.VIRTUAL_HOST);

        // 通过连接工厂获得连接
        final Connection connection = connectionFactory.newConnection();
        System.out.println("========Producer连接成功============");
        // 通过connection创建一个Channel
        final Channel channel = connection.createChannel();

        // 消息属性 deliveryMode(2)持久化存在 服务重启依然存在
        Map<String,Object> headers = new HashMap<>(16);
        headers.put("my1","111" );
        headers.put("my2","222" );

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                // 过期时间 ms 未消费过期自动删除
                .expiration("10000")
                .headers(headers)
                .build();

        // 通过channel发送5次数据
        IntStream.rangeClosed(1, 5).forEach((c)->{
                    String msg = "Hello RabbitMQ!";
                    try {
                        channel.basicPublish("", "test002",properties
                                , msg.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

         // close
        channel.close();
        connection.close();

    }

}

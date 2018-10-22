package cn.zyblogs.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Title: Producer.java
 * @Package cn.zyblogs.rabbitmq.quickstart
 * @Description: TODO
 * @Author ZhangYB
 * @Version V1.0
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建一个ConnectionFactory进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.32.144");
        connectionFactory.setPort(5672);
        // 设置虚拟主机
        connectionFactory.setVirtualHost("/");

        // 通过连接工厂获得连接
        final Connection connection = connectionFactory.newConnection();
        System.out.println("========Producer连接成功============");
        // 通过connection创建一个Channel
        final Channel channel = connection.createChannel();


        // 通过channel发送5次数据
        IntStream.rangeClosed(1, 5).forEach((c)->{
                    String msg = "Hello RabbitMQ!";
                    try {
                        channel.basicPublish("", "test002",null
                                , msg.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

         // close
//        channel.close();
//        connection.close();

    }

}

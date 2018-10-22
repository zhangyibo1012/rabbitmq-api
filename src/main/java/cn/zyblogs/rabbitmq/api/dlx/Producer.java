package cn.zyblogs.rabbitmq.api.dlx;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @Title: Producer.java
 * @Package cn.zyblogs.rabbitmq.api.confirm
 * @Description: TODO ctrl+alt+f6 方法后面显示%%
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

        // 通过connection创建一个channel
        final Channel channel = connection.createChannel();
        log.info("============Producer连接成功==============");


        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        IntStream.rangeClosed(1, 1).forEach((i) -> {
            try {
                // 发送消息
                String msg = "Hello RabbitMQ Send DLX message!" + i;
                Map<String, Object> headres = new HashMap<>(16);
                headres.put("num", i);
                //  deliveryMode(2)持久化存在 服务重启依然存在
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .deliveryMode(2)
                        .contentEncoding("UTF-8")
                        // 过期时间
                        .expiration("10000")
                        .build();

                channel.basicPublish(exchangeName, routingKey, properties, msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


    }
}

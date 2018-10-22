package cn.zyblogs.rabbitmq.api.confirm;

import cn.zyblogs.rabbitmq.api.constant.RabbitMqServer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Producer.java
 * @Package cn.zyblogs.rabbitmq.api.confirm
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

        // 通过connection创建一个channel
        final Channel channel = connection.createChannel();
        log.info("============Producer连接成功==============");

        // 指定消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        // 发送消息
        String msg = "Hello RabbitMQ Send confirm message!";
        channel.basicPublish(exchangeName, routingKey, null,msg.getBytes());

        //添加确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                // 成功
                log.info("=============ACK==============");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                // 失败
                log.info("=============NO ACK==============");
            }
        });


    }
}

package com.jaywong.rabbitmq;

/**
 * @author wangjie
 * @create 2023-03-13 17:48
 */
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ_Producer {
    /**
     * QUEUE_NAME: 消息队列名称
     */
    private final static String QUEUE_NAME  = "rk_queue_test";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取到连接以及mq通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        factory.setHost("localhost");
        factory.setPort(5672);
        Connection conn = factory.newConnection();
        // 创建一个频道
        Channel channel = conn.createChannel();
        // 指定一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        int prefetchCount = 1;

        //每个消费者发送确认信号之前，消息队列不发送下一个消息过来，一次只处理一个消息
        //限制发给同一个消费者不得超过1条消息
        channel.basicQos(prefetchCount);

        // 发送的消息
        for (int i = 0; i < 50; i++) {
            String message = "." + i;
            // 往队列中发出一条消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            Thread.sleep(i * 10);
        }
        // 关闭频道和连接
        channel.close();
        conn.close();
    }
}
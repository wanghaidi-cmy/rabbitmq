package com.whd.rabbitmq.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanghaidi
 * @create 2022-02-08 9:28
 * Topics模式：发消息
 */
public class Producer {
    /**
     * 定义交换机名称
     */
    static final String TOPICS_EXCHANGE = "topics_exchange";
    /**
     * 定义队列名
     */
    static final String TOPICS_QUEUE1 = "topics_queue1";
    /**
     * 定义队列名称
     */
    static final String TOPICS_QUEUE2 = "topics_queue2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        //1. 创建连接工厂（设置RabbitMQ的连接参数)
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //主机；默认localhost
        connectionFactory.setHost("localhost");
        //连接端口；默认5672
        connectionFactory.setPort(5672);
        //虚拟主机；默认/
        connectionFactory.setVirtualHost("/whd");
        //用户名；默认guest
        connectionFactory.setUsername("whd");
        //密码；默认guest
        connectionFactory.setPassword("whd");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //2.创建频道
        Channel channel = connection.createChannel();
        /**
         * 3.声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型
         */
        channel.exchangeDeclare(TOPICS_EXCHANGE, BuiltinExchangeType.TOPIC);
        /**
         * 4.声明队列
         * 参数1：队列名称
         * 参数2：是否定义持久化的队列
         * 参数3：是否独占本次连接
         * 参数4: 是否在不使用的时候自动删除队列
         * 参数5：队列其他参数
         */
        channel.queueDeclare(TOPICS_QUEUE1, true, false, false, null);
        channel.queueDeclare(TOPICS_QUEUE2, true, false, false, null);
        //5.队列绑定交换机
        channel.queueBind(TOPICS_QUEUE1,TOPICS_EXCHANGE,"topic.update");
        channel.queueBind(TOPICS_QUEUE1,TOPICS_EXCHANGE,"topic.delete");
        channel.queueBind(TOPICS_QUEUE2,TOPICS_EXCHANGE,"topic_insert");
        //6.发送消息
        String message = "新增了商品。Topic模式；routing key 为 topic.insert";
        channel.basicPublish(TOPICS_EXCHANGE, "topic.insert", null, message.getBytes());
        System.out.println("已发消息："+message);

        //发送消息
        message = "修改了商品。Topic模式；routing key 为 topic.update";
        channel.basicPublish(TOPICS_EXCHANGE, "topic.update", null, message.getBytes());
        System.out.println("已发消息："+message);

        //发送消息
        message = "删除了商品。Topic模式；routing key 为 topic.delete" ;
        channel.basicPublish(TOPICS_EXCHANGE,"topic.delete",null,message.getBytes());
        System.out.println("已发消息："+message);

        //7.关闭资源
        channel.close();
        connection.close();
    }
}

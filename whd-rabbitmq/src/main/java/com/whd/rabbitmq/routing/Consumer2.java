package com.whd.rabbitmq.routing;

import com.rabbitmq.client.*;
import com.whd.rabbitmq.simple.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanghaidi
 * @create 2022-02-07 19:41
 * 路由模式：接收消息
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = ConnectionUtil.getConnection();
        //2.声明频道
        Channel channel = connection.createChannel();
        //3.声明交换机
        channel.exchangeDeclare(Producer.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        /**
         * 4.声明队列
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其他参数
         */
        channel.queueDeclare(Producer.DIRECT_UPDATE, true, false, false, null);
        //5.队列绑定交换机、
        channel.queueBind(Producer.DIRECT_UPDATE, Producer.DIRECT_EXCHANGE, "update");
        //6.创建消费者，并处设置理消息
        /**
         * consumerTag 消息者标签，在channel.basicConsume时候可以指定
         * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志
         *          (收到消息失败后是否需要重新发送)
         * properties 属性信息
         * body 消息
         */
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("路由key为："+envelope.getRoutingKey());
                System.out.println("交换机为："+envelope.getExchange());
                System.out.println("消息id为："+envelope.getDeliveryTag());
                System.out.println("消费者--2接收道德消息为："+ new String(body,"utf-8"));
            }
        };
        //8.监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消
         *       息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(Producer.DIRECT_UPDATE, true, defaultConsumer);
    }
}

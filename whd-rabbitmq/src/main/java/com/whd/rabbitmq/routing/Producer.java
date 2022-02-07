package com.whd.rabbitmq.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.impl.ChannelN;
import com.whd.rabbitmq.simple.util.ConnectionUtil;
import sun.security.provider.certpath.BuildStep;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanghaidi
 * @create 2022-02-07 16:59
 * 路由模式：生产者发送消息
 * 路由模式的交换机类型为:direct
 */
public class Producer {
    /**
     * 定义交换机名称
     */
    static final String DIRECT_EXCHANGE = "direct_exchange";
    /**
     * 定义消息队列名称 DIRECT_INSERT
     */
    static final String DIRECT_INSERT = "direct_insert";
    /**
     * 定义消息队列名称 DIRECT_UPDATE
     */
    static final String DIRECT_UPDATE = "direct_update";


    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = ConnectionUtil.getConnection();
        //2.创建频道
        Channel channel = connection.createChannel();
        /**
         * 3.声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型 DIRECT fanout topic headers
         */
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        /**
         * 4.声明队列
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其他参数
         */
        channel.queueDeclare(DIRECT_INSERT, true, false, false, null);
        channel.queueDeclare(DIRECT_UPDATE, true, false, false, null);
        //5.队列绑定交换机
        channel.queueBind(DIRECT_INSERT, DIRECT_EXCHANGE, "insert");
        channel.queueBind(DIRECT_UPDATE, DIRECT_EXCHANGE, "update");
        //6.发送信息
        /**
         * 参数1：交换机名称
         * 参数2：路由key
         * 参数3：消息其他属性
         * 参数4: 消息内容
         */
        String message = "新增了商品。路由模式：routing key 为insert";
        channel.basicPublish(DIRECT_EXCHANGE, "insert", null, message.getBytes());
        System.out.println("已发送内容：" + message);
        //6.发送信息
        message = "修改了商品。路由模式：routing key 为update";
        /**
         * 参数1：交换机名称
         * 参数2：路由key
         * 参数3：消息其他属性
         * 参数4: 消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "update", null, message.getBytes());
        System.out.println("已发送内容：" + message);
        //7.关闭资源
        channel.close();
        connection.close();
    }
}

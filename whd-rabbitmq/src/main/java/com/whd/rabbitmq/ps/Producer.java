package com.whd.rabbitmq.ps;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.whd.rabbitmq.simple.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanghaidi
 * @create 2022-02-07 10:37
 * 发布与订阅模式：发送消息
 * 发布与订阅使用的交换机类型:fanout
 */
public class Producer {

    /**
     * 交换机名称
     */
    static final String FANOUT_EXCHANGE = "fanout_exchange";
    /**
     * 队列名称
     */
    static final String FANOUT_QUEUE_1 = "fanout_queue_1";
    /**
     * 队列名称
     */
    static final String FANOUT_QUEUE_2 = "fanout_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = ConnectionUtil.getConnection();
        //2.创建频道
        Channel channel = connection.createChannel();
        /**
         * 3.声明交换机
         *  参数1：交换机名称
         *  参数2：交换机类型，fanout,topic,direct,headers
         */
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        /**
         * 4.声明队列
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4:是否在不使用的时候自动删除队列
         * 参数5：队列其他参数
         */
        channel.queueDeclare(FANOUT_QUEUE_1, true, false, false, null);
        channel.queueDeclare(FANOUT_QUEUE_2, true, false,false,null);
        /**
         *  5.队列绑定交换机
         *  参数1：队列名称
         *  参数2:交换机名称
         *  参数3：路由名称
         */
        channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHANGE, "");
        channel.queueBind(FANOUT_QUEUE_2, FANOUT_EXCHANGE, "");
        for (int i = 1; i <= 10;i++) {
            //6.发送消息
            String message = "你好，赵露思，发布与订阅 ---->"+i;
            /**
             * 参数1：交换机名称；如果没有则指定空字符串（表示使用默认的交换机）
             * 参数2：路由key，简单模式中可以使用队列名称
             * 参数3：消息其它属性
             * 参数4：消息内容
             */
            channel.basicPublish(FANOUT_EXCHANGE, "", null, message.getBytes());
            System.out.println("已发送消息：" + message);
        }
        //7.关闭资源
        channel.close();
        connection.close();

    }
}

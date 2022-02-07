package com.whd.rabbitmq.simple.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanghaidi
 * @create 2022-02-05 19:58
 */
public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
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
        return connectionFactory.newConnection();
    }
}

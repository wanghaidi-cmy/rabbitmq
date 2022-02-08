package com.whd.rabbitmq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wanghaidi
 * @create 2022-02-08 15:37
 */
@Component
public class MyListener {
    /**
     * 监听某个队列的消息
     *
     * @param message 接收到的消息
     */
    @RabbitListener(queues = "item_queue")
    public void myListener1(String message) {
        System.out.println("消费者接收到的消息为：" + message);
    }
}

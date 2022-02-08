package com.whd.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wanghaidi
 * @create 2022-02-08 15:17
 */
@Configuration
public class RabbitMQConfig {
    /**
     * 交换机名称
     */
    public static final String ITEM_TOPIC_EXCHANGE = "item_topic_exchange";
    /**
     * 队列名称
     */
    public static final String ITEM_QUEUE = "item_queue";

    /**
     * 声明交换机
     */
    @Bean("itemTopicExchange")
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     * 声明队列
     */
    @Bean("itemQueue")
    public Queue itemQueue() {
        return QueueBuilder.durable(ITEM_QUEUE).build();
    }

    /**
     * 队列绑定交换机
     *
     * @param queue    队列
     * @param exchange 交换机名称
     * @return 返回值
     */
    @Bean
    public Binding itemQueueExchange(@Qualifier("itemQueue") Queue queue,
                                     @Qualifier("itemTopicExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();

    }
}

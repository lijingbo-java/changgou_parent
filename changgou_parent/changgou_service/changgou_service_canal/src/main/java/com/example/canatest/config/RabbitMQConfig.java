package com.example.canatest.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/29 21:29
 * @description ：队列和交换机的创建和绑定
 * @version: 1.0
 */
@Configuration//此配置文件只是进行创建和绑定交换机
public class RabbitMQConfig {
    //队列
    public static String ADD_ES_QUEUE = "add_es_queue";//修改ES库
    public static String ES_PAGE_EXCHANGE = "es_page_exchange";//交换机
    public static String CREATE_PAGE_QUEUE = "cretae_page_queue";//生成静态页

    @Bean//创建ES库操作队列
    public Queue esQueue() {
        return new Queue(ADD_ES_QUEUE);
    }

    @Bean//创建静态页队列
    public Queue pageQueue() {
        return new Queue(CREATE_PAGE_QUEUE);
    }

    @Bean//创建交换机
    public TopicExchange topicExchange() {
        return new TopicExchange(ES_PAGE_EXCHANGE);
    }

    @Bean//绑定ES操作队列
    public Binding bEsT() {
        return BindingBuilder.bind(esQueue()).to(topicExchange()).with("it.#");
    }

    @Bean//绑定page队列
    public Binding bPageT() {
        return BindingBuilder.bind(pageQueue()).to(topicExchange()).with("it.*");
    }

}

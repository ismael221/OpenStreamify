package com.ismael.movies.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "notificationQueue";
    public static final String EXCHANGE_NAME = "notificationExchange";
    public static final String ROUTING_KEY = "notificationRoutingKey";

    public static final String ALERT_QUEUE = "alerts_queue";
    public static final String ALERT_EXCHANGE_NAME = "alertExchange";
    public static final String ALERT_ROUTING_KEY = "alertRoutingKey";

    public static final String MINIO_QUEUE = "minio_queue";
    public static final String MINIO_EXCHANGE_NAME = "minioExchange";
    public static final String MINIO_ROUTING_KEY = "minioRoutingKey";

    public static final String EMAIL_QUEUE = "email_queue";
    public static final String EMAIL_EXCHANGE_NAME = "emailExchange";
    public static final String EMAIL_ROUTING_KEY = "emailRoutingKey";

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding notificationBinding(@Qualifier("notificationQueue") Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue alertQueue() {
        return new Queue(ALERT_QUEUE, true);
    }

    @Bean
    public DirectExchange alertExchange() {
        return new DirectExchange(ALERT_EXCHANGE_NAME);
    }

    @Bean
    public Binding alertBinding(@Qualifier("alertQueue") Queue alertQueue, DirectExchange alertExchange) {
        return BindingBuilder.bind(alertQueue).to(alertExchange).with(ALERT_ROUTING_KEY);
    }

    @Bean
    public Queue minioQueue(){
        return new Queue(MINIO_QUEUE,true);
    }

    @Bean
    public DirectExchange minioExchange(){
        return new DirectExchange(MINIO_EXCHANGE_NAME);
    }

    @Bean
    public Binding minioBinding(@Qualifier("minioQueue") Queue minioQueue, DirectExchange minioExchange){
        return BindingBuilder.bind(minioQueue).to(minioExchange).with(MINIO_ROUTING_KEY);
    }

    @Bean
    public Queue emailQueue(){return new Queue(EMAIL_QUEUE,true);}

    @Bean
    public DirectExchange emailExchange(){return new DirectExchange(EMAIL_EXCHANGE_NAME);}

    @Bean
    public Binding emailBinding(@Qualifier("emailQueue") Queue emailQueue, DirectExchange emailExchange){
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(EMAIL_ROUTING_KEY);
    }
}

package com.ismael.movies.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public static final String ALERT_EXCHANGE_NAME = "alertExchange"; // Nova exchange para alertas
    public static final String ALERT_ROUTING_KEY = "alertRoutingKey";

    public static final String MINIO_QUEUE = "minio_queue";
    public static final String MINIO_EXCHANGE_NAME = "minioExchange"; // Nova exchange para alertas
    public static final String MINIO_ROUTING_KEY = "minioRoutingKey";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true); // A fila será persistente
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE_NAME); // Exchange para notificações
    }

    @Bean
    public Binding notificationBinding(@Qualifier("notificationQueue") Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(ROUTING_KEY); // Vincula a fila à exchange de notificações
    }

    @Bean
    public Queue alertQueue() {
        return new Queue(ALERT_QUEUE, true); // A fila será persistente
    }

    @Bean
    public DirectExchange alertExchange() {
        return new DirectExchange(ALERT_EXCHANGE_NAME); // Nova exchange para alertas
    }

    @Bean
    public Binding alertBinding(@Qualifier("alertQueue") Queue alertQueue, DirectExchange alertExchange) {
        return BindingBuilder.bind(alertQueue).to(alertExchange).with(ALERT_ROUTING_KEY); // Vincula a fila à nova exchange de alertas
    }


    @Bean
    public Queue minioQueue() {
        return new Queue(MINIO_QUEUE, true); // A fila será persistente
    }

    @Bean
    public DirectExchange minioExchange() {
        return new DirectExchange(MINIO_EXCHANGE_NAME); // Nova exchange para alertas
    }

    @Bean
    public Binding minioBinding(@Qualifier("minioQueue") Queue minioQueue, DirectExchange minioExchange) {
        return BindingBuilder.bind(minioQueue).to(minioExchange).with(MINIO_ROUTING_KEY); // Vincula a fila à nova exchange de alertas
    }
}

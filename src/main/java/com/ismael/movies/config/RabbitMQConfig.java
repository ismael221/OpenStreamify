package com.ismael.movies.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "notificationQueue";
    public static final String EXCHANGE_NAME = "notificationExchange";
    public static final String ROUTING_KEY = "notificationRoutingKey";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding notificationBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}

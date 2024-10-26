package com.ismael.movies.services;

import com.ismael.movies.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationQueueService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendToNoticationsQueue(String message){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
    }


}

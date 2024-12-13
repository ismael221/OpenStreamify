package com.ismael.movies.services;

import com.ismael.movies.config.RabbitMQConfig;
import com.ismael.movies.model.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailQueueService {

    final
    RabbitTemplate rabbitTemplate;

    public EmailQueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailToQueue(EmailMessage emailMessage){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE_NAME,RabbitMQConfig.EMAIL_ROUTING_KEY,emailMessage);
    }

}

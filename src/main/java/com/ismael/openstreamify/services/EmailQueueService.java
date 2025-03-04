package com.ismael.openstreamify.services;

import com.ismael.openstreamify.config.RabbitMQConfig;
import com.ismael.openstreamify.model.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

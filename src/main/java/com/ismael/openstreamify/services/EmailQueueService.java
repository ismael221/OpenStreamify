package com.ismael.openstreamify.services;

import com.ismael.openstreamify.config.RabbitMQConfig;
import com.ismael.openstreamify.model.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailQueueService {


    private final QueueService queueService;

    public void sendEmailToQueue(EmailMessage emailMessage) {
        queueService.sendToQueue(RabbitMQConfig.EMAIL_EXCHANGE_NAME, RabbitMQConfig.EMAIL_ROUTING_KEY, String.valueOf(emailMessage));
    }

}

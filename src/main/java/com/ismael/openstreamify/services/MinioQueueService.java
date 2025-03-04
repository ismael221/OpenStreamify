package com.ismael.openstreamify.services;


import com.ismael.openstreamify.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MinioQueueService {

    final
    RabbitTemplate rabbitTemplate;

    public MinioQueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToMinioUploadingQueue(String message){
        rabbitTemplate.convertAndSend(RabbitMQConfig.MINIO_EXCHANGE_NAME, RabbitMQConfig.MINIO_ROUTING_KEY, message);
    }
}

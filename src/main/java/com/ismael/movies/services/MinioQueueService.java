package com.ismael.movies.services;


import com.ismael.movies.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinioQueueService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendToMinioUploadindQueue(String message){
        rabbitTemplate.convertAndSend(RabbitMQConfig.MINIO_EXCHANGE_NAME, RabbitMQConfig.MINIO_ROUTING_KEY, message);
    }
}

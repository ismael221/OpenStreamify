package com.ismael.movies.services;

import com.ismael.movies.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoProcessingQueueService {

    private final RabbitTemplate rabbitTemplate;

    public VideoProcessingQueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void queueForProcessing(String videoPath) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.VIDEO_PROCESSING_EXCHANGE_NAME, RabbitMQConfig.VIDEO_PROCESSING_ROUTING_KEY, videoPath);
    }
}

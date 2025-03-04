package com.ismael.openstreamify.services;

import com.ismael.openstreamify.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

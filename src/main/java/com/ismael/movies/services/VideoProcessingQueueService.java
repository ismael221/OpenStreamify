package com.ismael.movies.services;

import com.ismael.movies.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoProcessingQueueService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void queueForProcessing(String videoPath) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ALERT_EXCHANGE_NAME, RabbitMQConfig.ALERT_ROUTING_KEY, videoPath);
    }
}

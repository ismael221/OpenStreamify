package com.ismael.openstreamify.services;

import com.ismael.openstreamify.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoProcessingQueueService {

    private static Logger logger = LoggerFactory.getLogger(VideoProcessingQueueService.class);

    private final QueueService queueService;

    public void queueForProcessing(String videoPath) {
        logger.info("Processing video: " + videoPath);
        queueService.sendToQueue(RabbitMQConfig.VIDEO_PROCESSING_EXCHANGE_NAME, RabbitMQConfig.VIDEO_PROCESSING_ROUTING_KEY, videoPath);
    }
}

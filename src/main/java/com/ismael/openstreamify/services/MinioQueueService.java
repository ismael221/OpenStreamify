package com.ismael.openstreamify.services;


import com.ismael.openstreamify.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioQueueService {

    private static final Logger logger = LoggerFactory.getLogger(MinioQueueService.class);

    private final QueueService queueService;

    public void sendToMinioUploadingQueue(String message) {
        logger.info("Sending to minio uploading queue...");
        queueService.sendToQueue(RabbitMQConfig.MINIO_EXCHANGE_NAME, RabbitMQConfig.MINIO_ROUTING_KEY, message);
    }
}

package com.ismael.openstreamify.services;

import com.ismael.openstreamify.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueueService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationQueueService.class);

    private final QueueService queueService;

    public void sendToNoticationsQueue(String message) {
        logger.info("Sending notification to notications queue");
        queueService.sendToQueue(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
    }

}

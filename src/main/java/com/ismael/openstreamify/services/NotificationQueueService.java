package com.ismael.openstreamify.services;

import com.ismael.openstreamify.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueueService {

    private final QueueService queueService;

    public void sendToNoticationsQueue(String message) {
        queueService.sendToQueue(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
    }

}

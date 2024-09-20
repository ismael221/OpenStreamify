package com.ismael.movies.consumer;

import com.ismael.movies.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        // Enviar a notificação para o frontend via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}

package com.ismael.movies.services;

import com.ismael.movies.DTO.NotificationDTO;
import com.ismael.movies.config.RabbitMQConfig;
import com.ismael.movies.model.Notifications;
import com.ismael.movies.model.UserNotification;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.repository.NotificationsRepository;
import com.ismael.movies.repository.UserNotificationRepository;
import com.ismael.movies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Value("${TELEGRAM_BOT_TOKEN}")
    private String token;

    @Value("${TELEGRAM_CHAT_ID}")
    private String chatId;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserNotificationRepository userNotificationRepository;

    @Autowired
    ModelMapper modelMapper;

    public NotificationDTO convertToDTO(Notifications notifications){
        return  modelMapper.map(notifications,NotificationDTO.class);
    }

    @Transactional
    public void sendNotification(String message, List<UUID> userIds) {
        // Criar uma nova notificação
        Notifications notification = new Notifications();
        notification.setMessage(message);
        notification.setCreatedAt(new Date());

        notificationsRepository.save(notification);

        // Buscar os usuários com os IDs fornecidos
        List<User> users = userRepository.findAllById(userIds);

        for (User user: users){
            UserNotification userNotification = UserNotification.builder()
                    .user(user)
                    .notification(notification)
                    .visualized(false)
                    .build();
            userNotificationRepository.save(userNotification);
        }

        // Enviar a mensagem pelo RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
    }

    @Transactional
    public List<NotificationDTO> listNotificationsByUserId(UUID userId){
        List<Notifications> oldList = notificationsRepository.findByUserId(userId);
        List<NotificationDTO> notificationDTOS = oldList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return notificationDTOS;
    }
    //TODO FIX DE WRONG RELATIONSHIP BETWEEN THE NOTIFICATIONS AND THE USERS, BECAUSE WHEN SOMEONE READ THEIR NOTIFICATIONS IT CHANGES TO ALL THE USERS
    @Transactional
    public void markNotificationAsVisualized(UUID notificationId, UUID userId) {
        userNotificationRepository.markAsVisualized(notificationId, userId);
    }

    public void enviarMensagemTelegram(String mensagem) {

        String url = "https://api.telegram.org/bot" + token + "/sendMessage";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", mensagem);
        body.put("parse_mode", "Markdown");

        restTemplate.postForObject(url, body, String.class);
    }
}

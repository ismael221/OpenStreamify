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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Cacheable(cacheNames = "notifications")
public class NotificationService {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String token;

    @Value("${TELEGRAM_CHAT_ID}")
    private String chatId;

    final
    NotificationsRepository notificationsRepository;

    final
    UserRepository userRepository;

    final
    UserNotificationRepository userNotificationRepository;

    final
    ModelMapper modelMapper;

    final
    NotificationQueueService notificationQueueService;

    public NotificationService(NotificationsRepository notificationsRepository, UserRepository userRepository, UserNotificationRepository userNotificationRepository, ModelMapper modelMapper, NotificationQueueService notificationQueueService) {
        this.notificationsRepository = notificationsRepository;
        this.userRepository = userRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.modelMapper = modelMapper;
        this.notificationQueueService = notificationQueueService;
    }

    public NotificationDTO convertToDTO(Notifications notifications){
        return  modelMapper.map(notifications,NotificationDTO.class);
    }

    @Transactional
    @CacheEvict(cacheNames = "notifications-list",allEntries = true)
    public void sendNotification(String message, List<UUID> userIds) {
        // Create a new notification
        Notifications notification = new Notifications();
        notification.setMessage(message);
        notification.setCreatedAt(new Date());

        notificationsRepository.save(notification);

        // Search for users with the given IDs
        List<User> users = userRepository.findAllById(userIds);

        for (User user: users){
            UserNotification userNotification = UserNotification.builder()
                    .user(user)
                    .notification(notification)
                    .visualized(false)
                    .build();
            userNotificationRepository.save(userNotification);
        }
        notificationQueueService.sendToNoticationsQueue(message);
    }

    @Transactional
    @Cacheable(cacheNames = "notifications-list")
    public List<NotificationDTO> listNotificationsByUserId(UUID userId){
        List<Notifications> oldList = notificationsRepository.findByUserId(userId);
        List<NotificationDTO> notificationDTOS = oldList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return notificationDTOS;
    }

    @Transactional
    @CacheEvict(cacheNames = "notifications-list", key = "#userId",allEntries = true)
    public void markNotificationAsVisualized(UUID notificationId, UUID userId) {
        userNotificationRepository.markAsVisualized(notificationId, userId);
    }

    @Transactional
    @CacheEvict(cacheNames = "notifications-list", key = "#userId",allEntries = true)
    public void  markAllNotificationsAsVisualized(UUID userId){
        userNotificationRepository.markAllAsVisualized(userId);
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

    @Transactional
    @CacheEvict(cacheNames = "notifications-list", allEntries = true)
    public void notifyAllUsers(String message){
        Notifications notification = new Notifications();
        notification.setMessage(message);
        notification.setCreatedAt(new Date());

        notificationsRepository.save(notification);

        List<User> users = userRepository.findAll();
        for (User user: users){
            UserNotification userNotification = UserNotification.builder()
                    .user(user)
                    .notification(notification)
                    .visualized(false)
                    .build();
            userNotificationRepository.save(userNotification);
        }

        notificationQueueService.sendToNoticationsQueue(message);
    }
}

package com.ismael.movies.services;

import com.ismael.movies.DTO.NotificationDTO;
import com.ismael.movies.config.RabbitMQConfig;
import com.ismael.movies.model.Notifications;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.repository.NotificationsRepository;
import com.ismael.movies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    UserRepository userRepository;

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
        notification.setVisualized(false);

        // Buscar os usuários com os IDs fornecidos
        List<User> users = userRepository.findAllById(userIds);

        // Adicionar os usuários à notificação
        notification.setUsers(users);

        // Salvar a notificação (automaticamente salva o relacionamento ManyToMany)
        Notifications savedNotification = notificationsRepository.save(notification);

        // Agora associar a notificação aos usuários
        for (User user : users) {
            user.getNotifications().add(savedNotification);
            userRepository.save(user);  // Atualizar o usuário com a notificação associada
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
}

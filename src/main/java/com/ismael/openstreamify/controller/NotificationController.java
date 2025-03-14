package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.config.RabbitMQConfig;
import com.ismael.openstreamify.services.MinioQueueService;
import com.ismael.openstreamify.services.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notice")
public class NotificationController {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String token;

    @Value("${TELEGRAM_CHAT_ID}")
    private String chatId;

    final
    NotificationService notificationService;

    final
    MinioQueueService minioQueueService;

    private final RabbitTemplate rabbitTemplate;

    public NotificationController(RabbitTemplate rabbitTemplate, NotificationService notificationService, MinioQueueService minioQueueService, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationService = notificationService;
        this.minioQueueService = minioQueueService;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    final
    RabbitMQConfig rabbitMQConfig;


    @GetMapping("{rid}")
    public ResponseEntity<List> listNotificationByUserId(@PathVariable UUID rid){
        return ResponseEntity.ok(notificationService.listNotificationsByUserId(rid));
    }

    @PostMapping("{notification}/{user}")
    public ResponseEntity updateNotifications(@PathVariable UUID notification, @PathVariable UUID user){
        notificationService.markNotificationAsVisualized(notification,user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{user}")
    public ResponseEntity updateAllNotifications(@PathVariable UUID user){
        notificationService.markAllNotificationsAsVisualized(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/grafana")
    public ResponseEntity<String> interceptarNotificacao(@RequestBody Map<String, Object> payload) {
        // Extract and customize notification data from Grafana
        String estadoAlerta = (String) payload.get("state");
        String mensagem = (String) payload.get("message");
        String titulo = (String) payload.get("title");
        String urlGrafana = (String) payload.get("externalUrl");

        // Create a personalized message
        String mensagemPersonalizada = "🚨 *Grafana Alert* 🚨\n\n" +
                "*Título*: " + titulo + "\n" +
                "*Estado*: " + estadoAlerta + "\n" +
                "*Detalhes*: " + mensagem + "\n" +
                "[Ver mais no Grafana](" + urlGrafana + ")";

        // Send to Telegram bot
        notificationService.enviarMensagemTelegram(mensagemPersonalizada);

        return ResponseEntity.ok("Notificação interceptada e personalizada.");
    }

    @PostMapping
    public ResponseEntity notifyAllUsers(@RequestBody Map<String,Object> message){
        notificationService.notifyAllUsers((String) message.get("message"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //TODO FIX AND CREATE A PROPER QUEUE, THIS ONE IS INTENDED TO BE USED FOR ALERTING THE FFMPEG PROCESS
    @PostMapping("/send_alert")
    public String sendAlert(@RequestBody String alertData) {
        System.out.println(alertData);
        notificationService.enviarMensagemTelegram(alertData);
        rabbitTemplate.convertAndSend(RabbitMQConfig.VIDEO_PROCESSING_QUEUE, alertData);
        return "Alert sent to RabbitMQ";
    }
}

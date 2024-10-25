package com.ismael.movies.controller;

import com.ismael.movies.DTO.VideoDTO;
import com.ismael.movies.config.RabbitMQConfig;
import com.ismael.movies.model.Notifications;
import com.ismael.movies.services.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

    @Autowired
    NotificationService notificationService;

    private final RabbitTemplate rabbitTemplate;

    public NotificationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
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
        // Extrair e personalizar os dados da notificação do Grafana
        String estadoAlerta = (String) payload.get("state");
        String mensagem = (String) payload.get("message");
        String titulo = (String) payload.get("title");
        String urlGrafana = (String) payload.get("externalUrl");

        // Montar uma mensagem personalizada
        String mensagemPersonalizada = "🚨 *Grafana Alert* 🚨\n\n" +
                "*Título*: " + titulo + "\n" +
                "*Estado*: " + estadoAlerta + "\n" +
                "*Detalhes*: " + mensagem + "\n" +
                "[Ver mais no Grafana](" + urlGrafana + ")";

        // Enviar para o bot do Telegram
        notificationService.enviarMensagemTelegram(mensagemPersonalizada);

        return ResponseEntity.ok("Notificação interceptada e personalizada.");
    }

    @PostMapping
    public ResponseEntity notifyAllUsers(@RequestBody Map<String,Object> message){
        notificationService.notifyAllUsers((String) message.get("message"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send_alert")
    public String sendAlert(@RequestBody VideoDTO alertData) {
        // Envia o alerta para a fila RabbitMQ
        System.out.println(alertData);
        rabbitTemplate.convertAndSend(rabbitMQConfig.ALERT_QUEUE, alertData);
        return "Alert sent to RabbitMQ";
    }

}

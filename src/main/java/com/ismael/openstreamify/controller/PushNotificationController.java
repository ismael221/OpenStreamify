package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.services.PushNotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/push")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping("/send-notification")
    public void sendNotification(@RequestParam String title, @RequestParam String body) throws Exception {
        pushNotificationService.sendNotification(title, body);
    }
}

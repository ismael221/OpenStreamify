package com.ismael.movies.controller;

import com.ismael.movies.model.Notifications;
import com.ismael.movies.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notice")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("{rid}")
    public ResponseEntity<List> listNotificationByUserId(@PathVariable UUID rid){
        return ResponseEntity.ok(notificationService.listNotificationsByUserId(rid));
    }

    @PostMapping("{notification}/{user}")
    public ResponseEntity updateNotifications(@PathVariable UUID notification, @PathVariable UUID user){
        notificationService.markNotificationAsVisualized(notification,user);
        return new ResponseEntity(HttpStatus.OK);
    }
}

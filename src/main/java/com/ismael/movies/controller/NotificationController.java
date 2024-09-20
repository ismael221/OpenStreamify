package com.ismael.movies.controller;

import com.ismael.movies.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

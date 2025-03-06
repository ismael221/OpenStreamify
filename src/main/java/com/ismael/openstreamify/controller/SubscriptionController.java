package com.ismael.openstreamify.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1/push")
public class SubscriptionController {

    private final List<String> subscriptions = new CopyOnWriteArrayList<>();

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody String subscription) {
        subscriptions.add(subscription);
        System.out.println("Nova inscrição: " + subscription);
    }

    public List<String> getSubscriptions() {
        return subscriptions;
    }
}

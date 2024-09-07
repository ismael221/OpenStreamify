package com.ismael.movies.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ConfigRestController {
    @Value("${server.url}")
    private String serverUrl;

    @GetMapping("/config")
    public String getServerUrl() {
        return serverUrl;
    }

}

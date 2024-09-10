package com.ismael.movies.controller;

import com.ismael.movies.services.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    JavaMailSenderImpl mailSender;

    @PostMapping("/send")
    public String sendEmail(@RequestBody Map<String, String> request) throws  MessagingException, UnsupportedEncodingException {
        String from = request.get("from");
        String to = request.get("to");
        String subject = request.get("subject");
        String content = request.get("content");
        String result  = emailSenderService.sendEmail(to,from,subject,content);

        return result;
    }
}

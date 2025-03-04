package com.ismael.openstreamify.consumer;

import com.ismael.openstreamify.config.RabbitMQConfig;
import com.ismael.openstreamify.model.EmailMessage;
import com.ismael.openstreamify.services.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class EmailSenderConsumer {

    final
    EmailSenderService emailSenderService;

    public EmailSenderConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void sendEmailReceivedByQueue(EmailMessage emailMessage) throws MessagingException, UnsupportedEncodingException {
        emailSenderService.sendEmail(emailMessage);
    }
}

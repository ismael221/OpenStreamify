package com.ismael.openstreamify.consumer;

import com.ismael.openstreamify.config.RabbitMQConfig;
import com.ismael.openstreamify.model.EmailMessage;
import com.ismael.openstreamify.services.EmailSenderService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class EmailSenderConsumer {

    private Logger logger = LoggerFactory.getLogger(EmailSenderConsumer.class);

    private final EmailSenderService emailSenderService;

    public EmailSenderConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void sendEmailReceivedByQueue(EmailMessage emailMessage) throws MessagingException, UnsupportedEncodingException {

        logger.info("Received email message: {} ", emailMessage);
        try {
            emailSenderService.sendEmail(emailMessage);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", emailMessage.getTo(), e.getMessage());
        }

    }
}

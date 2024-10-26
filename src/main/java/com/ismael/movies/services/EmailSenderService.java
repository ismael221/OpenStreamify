package com.ismael.movies.services;

import com.ismael.movies.model.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailSenderService {
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private static final Logger logger = LoggerFactory.getLogger(FFmpegService.class);

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(EmailMessage emailMessage) throws MessagingException, UnsupportedEncodingException {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from, "OpenStreamify");
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getBody(), true);
            mailSender.send(message);
            logger.info("Email sent successfully.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.debug("Failed to send email. Error: " + e.getMessage());
        }
    }

}

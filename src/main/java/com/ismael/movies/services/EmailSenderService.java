package com.ismael.movies.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger logger = LoggerFactory.getLogger(FFmpegService.class);

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(
            String recipientEmail,
            String fromEmail,
            String subject,
            String content
    ) throws MessagingException, UnsupportedEncodingException {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromEmail, "OpenStreamify");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            logger.info("Email sent successfully.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.debug("Failed to send email. Error: " + e.getMessage());
        }
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification Code");
        message.setText("Your verification code is: " + verificationCode);
        mailSender.send(message);
    }
}

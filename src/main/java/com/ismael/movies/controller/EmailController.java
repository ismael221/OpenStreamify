package com.ismael.movies.controller;

import com.ismael.movies.config.RabbitMQConfig;
import com.ismael.movies.consumer.EmailSenderConsumer;
import com.ismael.movies.infra.security.TokenService;
import com.ismael.movies.model.EmailMessage;
import com.ismael.movies.model.UserVerification;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.model.VerificationCodeGenerator;
import com.ismael.movies.services.*;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/v1/email")
@CrossOrigin(origins = "http://ismael221.ddns.net") // Defina a origem que deve ser permitida
public class EmailController {

    @Autowired
    EmailQueueService emailQueueService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    VerificationCodeService verificationCodeService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${server.url}")
    private String serverUrl;

    @PostMapping("/send")
    public ResponseEntity sendEmail(@RequestBody Map<String, String> request) throws MessagingException, UnsupportedEncodingException {
        EmailMessage emailMessage = new EmailMessage(
          request.get("to"),
          request.get("subject"),
          request.get("content")
        );
        emailQueueService.sendEmailToQueue(emailMessage);
         return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send-reset-email")
    public ResponseEntity<Map<String, String>> sendResetEmail(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();
        try {
            UserDetails userFound = authorizationService.loadUserByUsername(email);
            if (userFound != null) {
                User user = userService.findUserByLogin(email);
                var resetPasswordToken = tokenService.generateToken(user);

                Locale currentLocale = LocaleContextHolder.getLocale();
                Context context = new Context(currentLocale );
                context.setVariable("baseUrl",serverUrl);
                context.setVariable("token", resetPasswordToken);
                String emailContent = templateEngine.process("mail-templates/password-reset", context);
                EmailMessage emailMessage = new EmailMessage(
                  userFound.getUsername(),
                  "Password Reset",
                  emailContent
                );
                emailQueueService.sendEmailToQueue(emailMessage);
                response.put("message", "Email sent successfully");
                return ResponseEntity.ok(response);
            }
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {

            String mensagemPersonalizada = "🚨 *Erro não tratado* 🚨\n\n" +
                    "*Título*: " + "Verifique as configurações do SMTP" + "\n" +
                    "*Status*: " + HttpStatus.INTERNAL_SERVER_ERROR + "\n" +
                    "*Mensagem*: " + e.getMessage() + "\n";

            notificationService.enviarMensagemTelegram(mensagemPersonalizada);

            response.put("message", "Failed to send email: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCodeEmail(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();
        VerificationCodeGenerator codeGenerator = new VerificationCodeGenerator();
        String verificationCode = codeGenerator.generateVerificationCode();
        UserDetails userFound = authorizationService.loadUserByUsername(email);
        if (userFound != null) {

            Locale currentLocale = LocaleContextHolder.getLocale();
            Context context = new Context(currentLocale);
            context.setVariable("baseUrl",serverUrl);
            context.setVariable("verificationCode", verificationCode);
            String emailContent = templateEngine.process("mail-templates/verification-code", context);

            UserVerification userVerification = new UserVerification();
            userVerification.setVerificationCode(verificationCode);
            userVerification.setEmail(userFound.getUsername());
            userVerification.setGeneratedAt(Instant.now());
            verificationCodeService.saveVerificationCode(userVerification);
            EmailMessage emailMessage = new EmailMessage(
                    userFound.getUsername(),
                    "Código de verificação",
                    emailContent
            );
            emailQueueService.sendEmailToQueue(emailMessage);

            response.put("message", "Email sent successfully");
            return ResponseEntity.ok(response);
        }
        response.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
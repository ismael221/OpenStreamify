package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.infra.security.TokenService;
import com.ismael.openstreamify.model.EmailMessage;
import com.ismael.openstreamify.model.UserVerification;
import com.ismael.openstreamify.model.Users.User;
import com.ismael.openstreamify.model.VerificationCodeGenerator;
import com.ismael.openstreamify.services.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/v1/email")
@CrossOrigin(origins = "http://ismael221.ddns.net") // Define the origin that should be allowed
public class EmailController {

    final
    EmailQueueService emailQueueService;

    final
    AuthorizationService authorizationService;

    final
    TokenService tokenService;

    final
    UserService userService;

    final
    NotificationService notificationService;

    final
    VerificationCodeService verificationCodeService;

    private final SpringTemplateEngine templateEngine;

    final
    RabbitTemplate rabbitTemplate;

    @Value("${server.url}")
    private String serverUrl;

    public EmailController(EmailQueueService emailQueueService, AuthorizationService authorizationService, TokenService tokenService, UserService userService, NotificationService notificationService, VerificationCodeService verificationCodeService, SpringTemplateEngine templateEngine, RabbitTemplate rabbitTemplate) {
        this.emailQueueService = emailQueueService;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.verificationCodeService = verificationCodeService;
        this.templateEngine = templateEngine;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/send")
    public ResponseEntity sendEmail(@RequestBody Map<String, String> request) {
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
package com.ismael.movies.controller;

import com.ismael.movies.infra.security.TokenService;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.services.AuthorizationService;
import com.ismael.movies.services.EmailSenderService;
import com.ismael.movies.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Value("${server.url}")
    private String serverUrl;

    @PostMapping("/send")
    public String sendEmail(@RequestBody Map<String, String> request) throws  MessagingException, UnsupportedEncodingException {
        String from = request.get("from");
        String to = request.get("to");
        String subject = request.get("subject");
        String content = request.get("content");
        String result  = emailSenderService.sendEmail(to,from,subject,content);

        return result;
    }

    @PostMapping("/send-reset-email")
    public ResponseEntity<Map<String, String>> sendResetEmail(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();
        try {
            UserDetails userFound = authorizationService.loadUserByUsername(email);
            if (userFound != null) {
                User user = userService.findUserByLogin(email);
                var resetPasswordToken = tokenService.generateToken(user);

                String emailContent = String.format("""
                    <!DOCTYPE html>
                    <html lang="pt-BR">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Redefinição de Senha</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f0f0f0;
                                margin: 0;
                                padding: 0;
                                color: #333;
                            }
                            .container {
                                width: 100%%;
                                max-width: 600px;
                                margin: 0 auto;
                                padding: 20px;
                                background-color: #ffffff;
                                border-radius: 8px;
                                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            }
                            .header {
                                text-align: center;
                                padding-bottom: 20px;
                            }
                            .header img {
                                max-width: 150px;
                            }
                            .content {
                                text-align: center;
                                padding: 20px;
                            }
                            .content h1 {
                                font-size: 24px;
                                margin-bottom: 20px;
                            }
                            .content p {
                                font-size: 16px;
                                margin-bottom: 20px;
                            }
                            .button {
                                display: inline-block;
                                padding: 15px 25px;
                                font-size: 16px;
                                color: #ffffff;
                                background-color: #007bff;
                                text-decoration: none;
                                border-radius: 5px;
                                margin-top: 20px;
                            }
                            .footer {
                                text-align: center;
                                font-size: 14px;
                                color: #888;
                                padding: 20px;
                                border-top: 1px solid #ddd;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <img src="https://example.com/logo.png" alt="Logo">
                            </div>
                            <div class="content">
                                <h1>Redefinição de Senha</h1>
                                <p>Olá,</p>
                                <p>Recebemos uma solicitação para redefinir a senha da sua conta. Clique no botão abaixo para redefinir sua senha:</p>
                                <a href="%s/reset-password?token=%s" class="button">Redefinir Senha</a>
                                <p>Se você não solicitou a redefinição de senha, ignore este email.</p>
                            </div>
                            <div class="footer">
                                &copy; 2024 Sua Empresa. Todos os direitos reservados.<br>
                                Endereço da Empresa | Contato: ismaeldenunes@gmail.com
                            </div>
                        </div>
                    </body>
                    </html>
                    """,serverUrl, resetPasswordToken);
                
                emailSenderService.sendEmail(userFound.getUsername(), "ismael@enrotech.com.br", "Password Reset", emailContent);
                response.put("message", "Email sent successfully");
                return ResponseEntity.ok(response);
            }
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}

package com.ismael.openstreamify.services;

import com.ismael.openstreamify.controller.SubscriptionController;
import com.ismael.openstreamify.model.SecurityConfig;
import com.ismael.openstreamify.model.SubscriptionHelper;
import nl.martijndwars.webpush.Notification;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Base64;
import java.util.List;

@Service
public class PushNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    private final SecurityConfig securityConfig;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final PushService pushService;
    private final SubscriptionController subscriptionController;

    public PushNotificationService(SecurityConfig securityConfig, SubscriptionController subscriptionController) throws Exception {
        this.securityConfig = securityConfig;
        this.subscriptionController = subscriptionController;

        KeyPair keyPair = generateVapidKeys();

        logger.info("Public Key: {}", securityConfig.getPublicKeyEncoded());
        logger.info("Private Key: {}", securityConfig.getPrivateKeyEncoded());

        this.pushService = new PushService(securityConfig.getPublicKeyEncoded(), securityConfig.getPrivateKeyEncoded());
    }

    public void sendNotification(String title, String body) {
        List<String> subscriptions = subscriptionController.getSubscriptions();
        for (String sub : subscriptions) {
            try {
                Subscription subscription = SubscriptionHelper.fromJson(sub);
                Notification notification = new Notification(
                        subscription,
                        "{\"title\": \"" + title + "\", \"body\": \"" + body + "\"}"
                );

                org.apache.http.HttpResponse response = pushService.send(notification);
                System.out.println(response);
                logger.info("Notificação enviada! Status: {}", response.getStatusLine());
            } catch (IllegalArgumentException e) {
                logger.error("Inscrição inválida: {}", sub, e);
            } catch (IOException e) {
                logger.error("Erro de rede ao enviar notificação: {}", e.getMessage(), e);
            } catch (GeneralSecurityException e) {
                logger.error("Erro de segurança ao enviar notificação: {}", e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Erro inesperado ao enviar notificação: {}", e.getMessage(), e);
            }
        }
    }

    private KeyPair generateVapidKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        return keyPairGenerator.generateKeyPair();
    }

    private String encodePublicKey(KeyPair keyPair) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                keyPair.getPublic().getEncoded()
        );
    }

    private String encodePrivateKey(KeyPair keyPair) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                keyPair.getPrivate().getEncoded()
        );
    }
}

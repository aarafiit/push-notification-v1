package com.example.pushnotificationv1.service;

import com.example.pushnotificationv1.dto.NotificationDto;
import com.example.pushnotificationv1.entity.PushNotification;
import com.example.pushnotificationv1.repo.PushNotificationRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }


    public String sendNotification(NotificationDto notificationDto) {
        try {
            Message message = Message.builder()
                                .setNotification(
                                Notification.builder()
                                        .setTitle(notificationDto.getTitle())
                                        .setBody(notificationDto.getBody())
                                        .build())
                                .setToken(notificationDto.getToken())
                                .build();
            PushNotification pushNotification = toEntity(notificationDto);
            pushNotificationRepository.save(pushNotification);
            return FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send notification", e);
        }
    }

    private PushNotification toEntity(NotificationDto notificationDto) {
        PushNotification entity = new PushNotification();
        entity.setTitle(notificationDto.getTitle());
        entity.setBody(notificationDto.getBody());
        entity.setToken(notificationDto.getToken());
        return entity;
    }

}

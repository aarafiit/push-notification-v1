package com.example.pushnotificationv1.controller;

import com.example.pushnotificationv1.dto.NotificationDto;
import com.example.pushnotificationv1.service.PushNotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final PushNotificationService pushNotificationService;

    @PostMapping("/send")
    public ResponseEntity<Map<String,Object>> sendNotification(@RequestBody NotificationDto notificationDto) {
        Map<String,Object> response = new HashMap<>();
        try {
            String message =  pushNotificationService.sendNotification(notificationDto);
            response.put("message","Notification sent successfully");
            response.put("success",true);
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

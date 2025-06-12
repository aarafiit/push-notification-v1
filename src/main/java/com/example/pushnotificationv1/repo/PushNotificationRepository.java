package com.example.pushnotificationv1.repo;

import com.example.pushnotificationv1.entity.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {
}

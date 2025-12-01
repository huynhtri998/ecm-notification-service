package com.trilabs94.ecm_notification.repository;

import com.trilabs94.ecm_notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
}

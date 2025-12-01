package com.trilabs94.ecm_notification.entity;

import com.trilabs94.ecm_notification.enums.NotificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private Integer orderId;
    private Integer paymentId;
}

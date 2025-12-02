package com.trilabs94.ecm_notification.repository;

import com.trilabs94.ecm_notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.OffsetDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    List<Notification> findByRecipientIgnoreCase(String recipient);

    List<Notification> findByOrderId(Long orderId);

    List<Notification> findByPaymentId(Long paymentId);

    List<Notification> findByCreatedAtBetween(OffsetDateTime from, OffsetDateTime to);
}

package com.trilabs94.ecm_notification.service;

import com.trilabs94.ecm_notification.dto.NotificationResponse;
import com.trilabs94.ecm_notification.dto.NotificationSearchRequest;
import com.trilabs94.ecm_notification.kafka.event.OrderCreatedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentCompletedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentFailedEvent;

import java.util.List;

public interface INotificationService {

    void handleOrderCreated(OrderCreatedEvent event);

    void handlePaymentCompleted(PaymentCompletedEvent event);

    void handlePaymentFailed(PaymentFailedEvent event);

    NotificationResponse getNotification(Long id);

    List<NotificationResponse> searchNotifications(NotificationSearchRequest request);

}

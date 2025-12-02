package com.trilabs94.ecm_notification.mapper;

import com.trilabs94.ecm_notification.dto.NotificationResponse;
import com.trilabs94.ecm_notification.entity.Notification;
import com.trilabs94.ecm_notification.kafka.event.OrderCreatedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentCompletedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toNotificationResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .sender(notification.getSender())
                .recipient(notification.getRecipient())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .orderId(notification.getOrderId())
                .paymentId(notification.getPaymentId())
                .build();
    }

    public Notification fromOrderCreatedEvent(OrderCreatedEvent event,
                                              String sender,
                                              String content) {
        if (event == null) {
            return null;
        }

        return Notification.builder()
                .sender(sender)
                .recipient(event.getCustomerEmail())
                .content(content)
                .orderId(event.getOrderId())
                .build();
    }

    public Notification fromPaymentCompletedEvent(PaymentCompletedEvent event,
                                                  String sender,
                                                  String content) {
        if (event == null) {
            return null;
        }

        return Notification.builder()
                .sender(sender)
                .recipient(event.getCustomerEmail())
                .content(content)
                .orderId(event.getOrderId())
                .paymentId(event.getPaymentId())
                .build();
    }

    public Notification fromPaymentFailedEvent(PaymentFailedEvent event,
                                               String sender,
                                               String content) {
        if (event == null) {
            return null;
        }

        return Notification.builder()
                .sender(sender)
                .recipient(event.getCustomerEmail())
                .content(content)
                .orderId(event.getOrderId())
                .paymentId(event.getPaymentId())
                .build();
    }
}
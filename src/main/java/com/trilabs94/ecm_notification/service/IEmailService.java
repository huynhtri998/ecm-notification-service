package com.trilabs94.ecm_notification.service;

import com.trilabs94.ecm_notification.kafka.event.OrderCreatedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentCompletedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentFailedEvent;

public interface IEmailService {

    String buildOrderCreatedHtml(OrderCreatedEvent event);

    String buildPaymentCompletedHtml(PaymentCompletedEvent event);

    String buildPaymentFailedHtml(PaymentFailedEvent event);

    String buildTestNotificationHtml(String content);
}

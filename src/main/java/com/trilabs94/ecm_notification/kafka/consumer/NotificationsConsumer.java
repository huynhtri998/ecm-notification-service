package com.trilabs94.ecm_notification.kafka.consumer;


import com.trilabs94.ecm_notification.kafka.event.OrderCreatedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentCompletedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentFailedEvent;
import com.trilabs94.ecm_notification.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationsConsumer {

    private final INotificationService notificationService;

    @KafkaListener(
            topics = "${app.kafka.topics.order-created}",
            groupId = "${app.kafka.consumer.notification-group-id:notification-service}",
            containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderReference={}, customerId={}",
                event.getOrderReference(), event.getCustomerId());
        notificationService.handleOrderCreated(event);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.payment-completed}",
            groupId = "${app.kafka.consumer.notification-group-id:notification-service}",
            containerFactory = "paymentCompletedKafkaListenerContainerFactory"
    )
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent paymentReference={}, orderId={}",
                event.getPaymentReference(), event.getOrderId());
        notificationService.handlePaymentCompleted(event);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.payment-failed}",
            groupId = "${app.kafka.consumer.notification-group-id:notification-service}",
            containerFactory = "paymentFailedKafkaListenerContainerFactory"
    )
    public void onPaymentFailed(PaymentFailedEvent event) {
        log.info("Received PaymentFailedEvent paymentReference={}, orderId={}",
                event.getPaymentReference(), event.getOrderId());
        notificationService.handlePaymentFailed(event);
    }
}

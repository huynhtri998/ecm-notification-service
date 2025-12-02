package com.trilabs94.ecm_notification.service.impl;

import com.trilabs94.ecm_notification.dto.NotificationResponse;
import com.trilabs94.ecm_notification.dto.NotificationSearchRequest;
import com.trilabs94.ecm_notification.entity.Notification;
import com.trilabs94.ecm_notification.kafka.event.OrderCreatedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentCompletedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentFailedEvent;
import com.trilabs94.ecm_notification.mapper.NotificationMapper;
import com.trilabs94.ecm_notification.repository.NotificationRepository;
import com.trilabs94.ecm_notification.service.IEmailService;
import com.trilabs94.ecm_notification.service.INotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final JavaMailSender mailSender;
    private final IEmailService emailTemplateService;

    @Value("${notification.default-sender:no-reply@ecommerce.local}")
    private String defaultSender;

    @Override
    public void handleOrderCreated(OrderCreatedEvent event) {
        if (event == null) {
            log.warn("Received null OrderCreatedEvent");
            return;
        }

        // HTML from Thymeleaf template
        String htmlContent = emailTemplateService.buildOrderCreatedHtml(event);
        String subject = "Order Confirmation - " + event.getOrderReference();

        Notification notification = notificationMapper.fromOrderCreatedEvent(
                event,
                defaultSender,
                htmlContent
        );

        notificationRepository.save(notification);

        sendEmail(notification.getSender(), notification.getRecipient(), subject, htmlContent);
    }

    @Override
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        if (event == null) {
            log.warn("Received null PaymentCompletedEvent");
            return;
        }

        String htmlContent = emailTemplateService.buildPaymentCompletedHtml(event);
        String subject = "Payment Successful - " + event.getPaymentReference();

        Notification notification = notificationMapper.fromPaymentCompletedEvent(
                event,
                defaultSender,
                htmlContent
        );

        notificationRepository.save(notification);

        sendEmail(notification.getSender(), notification.getRecipient(), subject, htmlContent);
    }

    @Override
    public void handlePaymentFailed(PaymentFailedEvent event) {
        if (event == null) {
            log.warn("Received null PaymentFailedEvent");
            return;
        }

        String htmlContent = emailTemplateService.buildPaymentFailedHtml(event);
        String subject = "Payment Failed - " + event.getPaymentReference();

        Notification notification = notificationMapper.fromPaymentFailedEvent(
                event,
                defaultSender,
                htmlContent
        );

        notificationRepository.save(notification);

        sendEmail(notification.getSender(), notification.getRecipient(), subject, htmlContent);
    }

    @Override
    public NotificationResponse getNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));

        return notificationMapper.toNotificationResponse(notification);
    }

    @Override
    public List<NotificationResponse> searchNotifications(NotificationSearchRequest request) {
        Specification<Notification> spec = buildSearchSpecification(request);

        List<Notification> notifications = notificationRepository.findAll(spec);

        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }


    private Specification<Notification> buildSearchSpecification(NotificationSearchRequest request) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (request.getRecipient() != null && !request.getRecipient().isBlank()) {
                predicates.getExpressions().add(
                        cb.equal(cb.lower(root.get("recipient")), request.getRecipient().toLowerCase())
                );
            }

            if (request.getOrderId() != null) {
                predicates.getExpressions().add(
                        cb.equal(root.get("orderId"), request.getOrderId())
                );
            }

            if (request.getPaymentId() != null) {
                predicates.getExpressions().add(
                        cb.equal(root.get("paymentId"), request.getPaymentId())
                );
            }

            if (request.getFromCreatedAt() != null) {
                predicates.getExpressions().add(
                        cb.greaterThanOrEqualTo(root.get("createdAt"), request.getFromCreatedAt())
                );
            }

            if (request.getToCreatedAt() != null) {
                predicates.getExpressions().add(
                        cb.lessThanOrEqualTo(root.get("createdAt"), request.getToCreatedAt())
                );
            }

            return predicates;
        };
    }

    private void sendEmail(String sender, String recipient, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            StandardCharsets.UTF_8.name());

            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("HTML email sent from {} to {} with subject '{}'", sender, recipient, subject);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", recipient, e.getMessage(), e);
        }
    }
}
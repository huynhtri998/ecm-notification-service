package com.trilabs94.ecm_notification.service.impl;

import com.trilabs94.ecm_notification.kafka.event.OrderCreatedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentCompletedEvent;
import com.trilabs94.ecm_notification.kafka.event.PaymentFailedEvent;
import com.trilabs94.ecm_notification.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final TemplateEngine templateEngine;

    @Override
    public String buildOrderCreatedHtml(OrderCreatedEvent event) {
        Context ctx = new Context();

        String customerName = (event.getCustomerFirstName() != null ? event.getCustomerFirstName() : "") +
                (event.getCustomerLastName() != null ? " " + event.getCustomerLastName() : "");

        ctx.setVariable("customerName", customerName.trim());
        ctx.setVariable("orderReference", event.getOrderReference());
        ctx.setVariable("totalAmount", event.getTotalAmount());
        ctx.setVariable("createdAt", event.getCreatedAt());

        return templateEngine.process("order-confirmation", ctx);
    }

    @Override
    public String buildPaymentCompletedHtml(PaymentCompletedEvent event) {
        Context ctx = new Context();

        ctx.setVariable("orderReference", event.getOrderReference());
        ctx.setVariable("paymentReference", event.getPaymentReference());
        ctx.setVariable("amount", event.getAmount());
        ctx.setVariable("paidAt", event.getPaidAt());

        return templateEngine.process("payment-success", ctx);
    }

    @Override
    public String buildPaymentFailedHtml(PaymentFailedEvent event) {
        Context ctx = new Context();

        String reason = (event.getFailureReason() != null && !event.getFailureReason().isBlank())
                ? event.getFailureReason()
                : "Unknown reason";

        ctx.setVariable("orderReference", event.getOrderReference());
        ctx.setVariable("paymentReference", event.getPaymentReference());
        ctx.setVariable("amount", event.getAmount());
        ctx.setVariable("failedAt", event.getFailedAt());
        ctx.setVariable("failureReason", reason);

        return templateEngine.process("payment-failed", ctx);
    }

    @Override
    public String buildTestNotificationHtml(String content) {
        Context ctx = new Context();
        ctx.setVariable("content", content);

        return templateEngine.process("test-notification", ctx);
    }
}

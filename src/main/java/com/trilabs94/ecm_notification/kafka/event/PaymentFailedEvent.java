package com.trilabs94.ecm_notification.kafka.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEvent {

    private Long paymentId;

    private Long orderId;

    private String orderReference;

    private String paymentReference;

    private BigDecimal amount;

    private String customerEmail;

    private String failureReason;

    private OffsetDateTime failedAt;
}
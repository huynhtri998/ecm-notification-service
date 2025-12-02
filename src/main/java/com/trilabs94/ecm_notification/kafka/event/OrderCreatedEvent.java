package com.trilabs94.ecm_notification.kafka.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent {

    private Long orderId;

    private Long customerId;

    private String customerEmail;

    private String customerFirstName;

    private String customerLastName;

    private String orderReference;

    private BigDecimal totalAmount;

    private OffsetDateTime createdAt;
}
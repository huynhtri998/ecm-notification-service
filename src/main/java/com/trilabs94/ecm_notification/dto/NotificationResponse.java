package com.trilabs94.ecm_notification.dto;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class NotificationResponse {

    Long id;

    String sender;

    String recipient;

    String content;

    OffsetDateTime createdAt;

    Long orderId;

    Long paymentId;
}

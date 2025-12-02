package com.trilabs94.ecm_notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSearchRequest {

    @Email(message = "Recipient must be a valid email")
    private String recipient;

    @Min(value = 1, message = "orderId must be positive")
    private Long orderId;

    @Min(value = 1, message = "paymentId must be positive")
    private Long paymentId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime fromCreatedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime toCreatedAt;
}

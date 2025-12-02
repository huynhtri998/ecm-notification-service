package com.trilabs94.ecm_notification.controller;

import com.trilabs94.ecm_notification.dto.NotificationResponse;
import com.trilabs94.ecm_notification.dto.NotificationSearchRequest;
import com.trilabs94.ecm_notification.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Validated
@Schema(
        name = "Notification Controller",
        description = "APIs for managing notifications"
)
public class NotificationController {

    private final INotificationService notificationService;

    @Operation(
            summary = "Get Notification by ID",
            description = "Retrieve notification details using the notification ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notification not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable @Min(1) Long id) {
        NotificationResponse response = notificationService.getNotification(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Search Notifications",
            description = "Search for notifications based on various criteria."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Notifications retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No notifications found matching the criteria"
            )
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> searchNotifications(@Valid NotificationSearchRequest request
    ) {
        return ResponseEntity.ok(notificationService.searchNotifications(request));
    }
}
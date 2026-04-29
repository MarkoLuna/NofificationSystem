package com.notification.notification.dto;

import com.notification.api.model.NotificationChannel;

import io.swagger.v3.oas.annotations.media.Schema;

import com.notification.api.model.NotificationCategory;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    @Schema(description = "Channel to send notification to")
    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    @Schema(description = "Category of notification")
    @NotNull(message = "Category is required")
    private NotificationCategory category;

    @Schema(description = "Message to send",
            example = "notification message")
    @NotEmpty(message = "Message is required")
    private String message;
}

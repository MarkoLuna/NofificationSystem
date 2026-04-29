package com.notification.api.dto;

import com.notification.api.model.NotificationCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Kafka message payload published by notification-service
 * and consumed by channel services (email, sms, push).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String message;
    private NotificationCategory category;
    private String userName;
}

package com.notification.notification.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.notification.dto.NotificationRequest;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.service.NotificationStrategy;
import com.notification.notification.config.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Strategy for sending push notifications.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class PushNotificationStrategy implements NotificationStrategy {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;
    private final KafkaTopicProperties topicProperties;

    @Override
    public void send(NotificationRequest request, String userName) {
        log.info("Sending PUSH from user {}: [{}] {}",
                userName, request.getCategory(), request.getMessage());
        
        NotificationMessage message = NotificationMessage.builder()
                .message(request.getMessage())
                .category(request.getCategory())
                .userName(userName)
                .build();
        
        kafkaTemplate.send(topicProperties.getPush(), message);
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return NotificationChannel.PUSH == channel;
    }
}

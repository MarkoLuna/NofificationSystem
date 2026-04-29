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
 * Strategy for sending email notifications.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class EmailNotificationStrategy implements NotificationStrategy {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;
    private final KafkaTopicProperties topicProperties;

    @Override
    public void send(NotificationRequest request, String userName) {
        log.info("Sending EMAIL from user {}: [{}] {}",
                userName, request.getCategory(), request.getMessage());
        
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .message(request.getMessage())
                .category(request.getCategory())
                .userName(userName)
                .build();

        kafkaTemplate.send(topicProperties.getEmail(), notificationMessage)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent message=[{}] with offset=[{}]",
                                result.getProducerRecord().value(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Unable to send message due to {} ", ex.getMessage());
                    }
            });
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return NotificationChannel.EMAIL == channel;
    }
}

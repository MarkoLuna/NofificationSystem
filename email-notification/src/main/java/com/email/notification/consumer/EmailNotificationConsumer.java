package com.email.notification.consumer;

import com.email.notification.service.NotificationService;
import com.notification.api.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for email notification messages.
 * Listens to the email-notification.v1 topic and processes incoming messages.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class EmailNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${notification.kafka.topics.email}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "${spring.kafka.consumer.concurrency:1}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(NotificationMessage message) {
        log.debug("Received email notification: user={}, category={}, message={}",
                message.getUserName(), message.getCategory(), message.getMessage());

        notificationService.processNotificationMessage(message);
    }
}

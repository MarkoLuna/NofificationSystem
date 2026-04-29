package com.sms.sms.consumer;

import com.notification.api.dto.NotificationMessage;
import com.sms.sms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for sms notification messages.
 * Listens to the sms-notification.v1 topic and processes incoming messages.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class SmsNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${notification.kafka.topics.sms}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.consumer.concurrency:1}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(NotificationMessage message) {
        log.debug("Received sms notification: user={}, category={}, message={}",
                message.getUserName(), message.getCategory(), message.getMessage());

        notificationService.processNotificationMessage(message);
    }
}

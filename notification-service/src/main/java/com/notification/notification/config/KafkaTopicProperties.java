package com.notification.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "notification.kafka.topics")
public class KafkaTopicProperties {
    private String email;
    private String sms;
    private String push;
}

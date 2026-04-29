package com.notification.notification.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.config.KafkaTopicProperties;
import com.notification.notification.dto.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsNotificationStrategyTest {

    @Captor
    private ArgumentCaptor<NotificationMessage> messageCaptor;

    @Mock
    private KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @Mock
    private KafkaTopicProperties topicProperties;

    @InjectMocks
    private SmsNotificationStrategy smsNotificationStrategy;

    private static final String TOPIC_SMS = "sms-notification.v1";
    private NotificationRequest sampleRequest;
    private String userName = "testUser";

    @BeforeEach
    void setUp() {
        sampleRequest = NotificationRequest.builder()
                .channel(NotificationChannel.SMS)
                .category(NotificationCategory.SPORTS)
                .message("Test SMS content")
                .build();
    }

    @Test
    @DisplayName("Should return true when channel is SMS")
    void supports_ShouldReturnTrue_WhenChannelIsSms() {
        assertThat(smsNotificationStrategy.supports(NotificationChannel.SMS)).isTrue();
    }

    @Test
    @DisplayName("Should return false when channel is not SMS")
    void supports_ShouldReturnFalse_WhenChannelIsNotSms() {
        assertThat(smsNotificationStrategy.supports(NotificationChannel.EMAIL)).isFalse();
        assertThat(smsNotificationStrategy.supports(NotificationChannel.PUSH)).isFalse();
    }

    @Test
    @DisplayName("Should send message to correct Kafka topic")
    void send_ShouldSendMessageToKafka_WhenValidRequest() {
        when(topicProperties.getSms()).thenReturn(TOPIC_SMS);

        smsNotificationStrategy.send(sampleRequest, userName);

        verify(kafkaTemplate).send(eq(TOPIC_SMS), messageCaptor.capture());
        NotificationMessage capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage)
                .returns(sampleRequest.getMessage(), NotificationMessage::getMessage)
                .returns(sampleRequest.getCategory(), NotificationMessage::getCategory)
                .returns(userName, NotificationMessage::getUserName);
    }
}

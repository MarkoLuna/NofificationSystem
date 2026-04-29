package com.notification.notification.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.notification.dto.NotificationRequest;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.config.KafkaTopicProperties;
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
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationStrategyTest {

    @Captor
    private ArgumentCaptor<NotificationMessage> messageCaptor;

    @Mock
    private KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @Mock
    private KafkaTopicProperties topicProperties;

    @InjectMocks
    private EmailNotificationStrategy emailNotificationStrategy;

    private static final String TOPIC_EMAIL = "email-notification.v1";
    private NotificationRequest sampleRequest;
    private String userName = "testUser";

    @BeforeEach
    void setUp() {
        sampleRequest = NotificationRequest.builder()
                .channel(NotificationChannel.EMAIL)
                .category(NotificationCategory.SPORTS)
                .message("Test email content")
                .build();
    }

    @Test
    @DisplayName("Should return true when channel is EMAIL")
    void supports_ShouldReturnTrue_WhenChannelIsEmail() {
        assertThat(emailNotificationStrategy.supports(NotificationChannel.EMAIL)).isTrue();
    }

    @Test
    @DisplayName("Should return false when channel is not EMAIL")
    void supports_ShouldReturnFalse_WhenChannelIsNotEmail() {
        assertThat(emailNotificationStrategy.supports(NotificationChannel.SMS)).isFalse();
        assertThat(emailNotificationStrategy.supports(NotificationChannel.PUSH)).isFalse();
    }

    @Test
    @DisplayName("Should send message to correct Kafka topic")
    void send_ShouldSendMessageToKafka_WhenValidRequest() {
        when(topicProperties.getEmail()).thenReturn(TOPIC_EMAIL);
        CompletableFuture<SendResult<String, NotificationMessage>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq(TOPIC_EMAIL), any(NotificationMessage.class))).thenReturn(future);

        emailNotificationStrategy.send(sampleRequest, userName);

        verify(kafkaTemplate).send(eq(TOPIC_EMAIL), messageCaptor.capture());
        NotificationMessage capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage)
                .returns(sampleRequest.getMessage(), NotificationMessage::getMessage)
                .returns(sampleRequest.getCategory(), NotificationMessage::getCategory)
                .returns(userName, NotificationMessage::getUserName);
    }

    @Test
    @DisplayName("Should handle Kafka completion callback successfully")
    void send_ShouldHandleCompletion_WhenKafkaSucceeds() {
        when(topicProperties.getEmail()).thenReturn(TOPIC_EMAIL);
        SendResult<String, NotificationMessage> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, NotificationMessage>> future = CompletableFuture
                .completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), any())).thenReturn(future);

        emailNotificationStrategy.send(sampleRequest, userName);

        verify(kafkaTemplate).send(anyString(), any());
    }
}

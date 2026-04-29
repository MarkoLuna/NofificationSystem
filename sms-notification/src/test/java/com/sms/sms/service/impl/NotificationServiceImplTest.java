package com.sms.sms.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.sms.sms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

        @Mock
        private UserService userService;

        @InjectMocks
        private NotificationServiceImpl notificationService;

        private NotificationMessage sampleMessage;
        private UserDto subscriber1;
        private UserDto creator;

        @BeforeEach
        void setUp() {
                sampleMessage = NotificationMessage.builder()
                                .userName("creatorUser")
                                .category(NotificationCategory.SPORTS)
                                .message("Test SMS Notification")
                                .build();

                subscriber1 = UserDto.builder()
                                .id("123456789")
                                .name("user1")
                                .phoneNumber("123456789")
                                .build();

                creator = UserDto.builder()
                                .id("987654321")
                                .name("creatorUser")
                                .phoneNumber("987654321")
                                .build();
        }

        @Test
        @DisplayName("Should send SMS notifications to all subscribed users except the creator")
        void processNotificationMessage_ShouldSendSmsNotifications_WhenSubscribersExist() {
                List<UserDto> subscribers = List.of(subscriber1, creator);
                when(userService.getUsersByChannelAndCategory(eq(NotificationChannel.SMS),
                                eq(NotificationCategory.SPORTS)))
                                .thenReturn(subscribers);
                notificationService.processNotificationMessage(sampleMessage);
                verify(userService).getUsersByChannelAndCategory(NotificationChannel.SMS, NotificationCategory.SPORTS);
        }

        @Test
        @DisplayName("Should handle empty subscriber list gracefully")
        void processNotificationMessage_ShouldDoNothing_WhenNoSubscribersFound() {
                when(userService.getUsersByChannelAndCategory(any(), any()))
                                .thenReturn(Collections.emptyList());
                notificationService.processNotificationMessage(sampleMessage);
                verify(userService).getUsersByChannelAndCategory(NotificationChannel.SMS, NotificationCategory.SPORTS);
        }
}

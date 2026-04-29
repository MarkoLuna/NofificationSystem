package com.email.notification.service.impl;

import com.email.notification.service.UserService;
import com.notification.api.dto.NotificationMessage;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
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
        private UserDto subscriber2;
        private UserDto creator;

        @BeforeEach
        void setUp() {
                sampleMessage = NotificationMessage.builder()
                                .userName("creatorUser")
                                .category(NotificationCategory.SPORTS)
                                .message("Test Message")
                                .build();

                subscriber1 = UserDto.builder()
                                .name("user1")
                                .email("user1@example.com")
                                .build();

                subscriber2 = UserDto.builder()
                                .name("user2")
                                .email("user2@example.com")
                                .build();

                creator = UserDto.builder()
                                .name("creatorUser")
                                .email("creator@example.com")
                                .build();
        }

        @Test
        @DisplayName("Should send emails to all subscribed users except the creator")
        void processNotificationMessage_ShouldSendEmailsToSubscribedUsers_WhenCreatorIsAlsoSubscribed() {
                List<UserDto> subscribers = List.of(subscriber1, subscriber2, creator);
                when(userService.getUsersByChannelAndCategory(eq(NotificationChannel.EMAIL),
                                eq(NotificationCategory.SPORTS)))
                                .thenReturn(subscribers);
                notificationService.processNotificationMessage(sampleMessage);
                verify(userService).getUsersByChannelAndCategory(NotificationChannel.EMAIL,
                                NotificationCategory.SPORTS);
        }

        @Test
        @DisplayName("Should send emails to all users when creator is not in the subscriber list")
        void processNotificationMessage_ShouldSendEmailsToAll_WhenCreatorIsNotSubscribed() {
                List<UserDto> subscribers = List.of(subscriber1, subscriber2);
                when(userService.getUsersByChannelAndCategory(eq(NotificationChannel.EMAIL),
                                eq(NotificationCategory.SPORTS)))
                                .thenReturn(subscribers);
                notificationService.processNotificationMessage(sampleMessage);
                verify(userService).getUsersByChannelAndCategory(NotificationChannel.EMAIL,
                                NotificationCategory.SPORTS);
        }

        @Test
        @DisplayName("Should handle empty subscriber list gracefully")
        void processNotificationMessage_ShouldDoNothing_WhenNoUsersSubscribed() {
                when(userService.getUsersByChannelAndCategory(any(), any()))
                                .thenReturn(Collections.emptyList());
                notificationService.processNotificationMessage(sampleMessage);
                verify(userService).getUsersByChannelAndCategory(NotificationChannel.EMAIL,
                                NotificationCategory.SPORTS);
        }
}

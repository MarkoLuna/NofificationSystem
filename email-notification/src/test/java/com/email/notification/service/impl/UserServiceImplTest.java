package com.email.notification.service.impl;

import com.notification.api.client.UsersClient;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UsersClient usersClient;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should return list of users when client succeeds")
    void getUsersByChannelAndCategory_ShouldReturnUsers_WhenClientSucceeds() {
        NotificationChannel channel = NotificationChannel.EMAIL;
        NotificationCategory category = NotificationCategory.SPORTS;
        List<UserDto> expectedUsers = List.of(
                UserDto.builder().name("user1").email("user1@example.com").build(),
                UserDto.builder().name("user2").email("user2@example.com").build());

        when(usersClient.getUsersByChannelAndCategory(channel, category)).thenReturn(expectedUsers);

        List<UserDto> actualUsers = userService.getUsersByChannelAndCategory(channel, category);

        assertThat(actualUsers).hasSize(2);
        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    @DisplayName("Should return empty list when client throws exception")
    void getUsersByChannelAndCategory_ShouldReturnEmptyList_WhenClientThrowsException() {
        NotificationChannel channel = NotificationChannel.EMAIL;
        NotificationCategory category = NotificationCategory.FINANCE;

        when(usersClient.getUsersByChannelAndCategory(channel, category))
                .thenThrow(new RuntimeException("API connection failure"));

        List<UserDto> actualUsers = userService.getUsersByChannelAndCategory(channel, category);

        assertThat(actualUsers).isEmpty();
        assertThat(actualUsers).isNotNull();
    }
}

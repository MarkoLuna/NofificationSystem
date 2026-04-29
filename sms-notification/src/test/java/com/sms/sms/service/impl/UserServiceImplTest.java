package com.sms.sms.service.impl;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UsersClient usersClient;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should delegate user lookup to UsersClient")
    void getUsersByChannelAndCategory_ShouldCallClient() {
        NotificationChannel channel = NotificationChannel.SMS;
        NotificationCategory category = NotificationCategory.SPORTS;
        List<UserDto> expectedUsers = List.of(UserDto.builder().id("12345").name("user1").build());
        when(usersClient.getUsersByChannelAndCategory(channel, category)).thenReturn(expectedUsers);
        List<UserDto> result = userService.getUsersByChannelAndCategory(channel, category);
        assertThat(result).isEqualTo(expectedUsers);
        verify(usersClient).getUsersByChannelAndCategory(channel, category);
    }
}

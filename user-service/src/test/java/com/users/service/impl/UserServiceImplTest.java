package com.users.service.impl;

import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("Should return user by ID when user exists")
    void getUserById_ShouldReturnUser_WhenIdExists() {
        Optional<UserDto> result = userService.getUserById("1");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("john");
    }

    @Test
    @DisplayName("Should return empty optional when user ID does not exist")
    void getUserById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Optional<UserDto> result = userService.getUserById("999");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return user by name when user exists")
    void getUserByName_ShouldReturnUser_WhenNameExists() {
        Optional<UserDto> result = userService.getUserByName("mike");
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("2");
    }

    @Test
    @DisplayName("Should return users by subscribed category")
    void getUsersBySubscribedCategory_ShouldReturnCorrectUsers() {
        List<UserDto> result = userService.getUsersBySubscribedCategory(NotificationCategory.SPORTS);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("john");
    }

    @Test
    @DisplayName("Should return users by notification channel")
    void getUsersByChannel_ShouldReturnCorrectUsers() {
        List<UserDto> result = userService.getUsersByChannel(NotificationChannel.EMAIL);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return users by both channel and category")
    void getUsersByChannelAndCategory_ShouldReturnCorrectUsers() {
        List<UserDto> result = userService.getUsersByChannelAndCategory(NotificationChannel.SMS,
                NotificationCategory.SPORTS);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("john");
    }

    @Test
    @DisplayName("Should return empty list when no users match channel and category")
    void getUsersByChannelAndCategory_ShouldReturnEmpty_WhenNoMatches() {
        List<UserDto> result = userService.getUsersByChannelAndCategory(NotificationChannel.PUSH,
                NotificationCategory.SPORTS);
        assertThat(result).isEmpty();
    }
}

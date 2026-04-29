package com.sms.sms.service.impl;

import com.notification.api.client.UsersClient;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.sms.sms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link UserService}.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersClient usersClient;

    @Override
    public List<UserDto> getUsersByChannelAndCategory(NotificationChannel channel, NotificationCategory category) {
        return usersClient.getUsersByChannelAndCategory(channel, category);
    }
}

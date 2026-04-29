package com.push.notification.service.impl;

import com.notification.api.client.UsersClient;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.push.notification.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersClient usersClient;

    @Override
    public List<UserDto> getUsersByChannelAndCategory(NotificationChannel channel, NotificationCategory category) {
        return usersClient.getUsersByChannelAndCategory(channel, category);
    }
}

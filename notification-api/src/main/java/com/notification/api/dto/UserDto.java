package com.notification.api.dto;

import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private List<NotificationChannel> channels;
    private List<NotificationCategory> subscribedCategories;
}

package com.push.notification.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.model.NotificationChannel;
import com.push.notification.service.NotificationService;
import com.push.notification.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserService userService;

    @Override
    public void processNotificationMessage(NotificationMessage message) {
        log.info("Sending push notification from user '{}': [{}] {}",
                message.getUserName(), message.getCategory(), message.getMessage());

        userService.getUsersByChannelAndCategory(NotificationChannel.PUSH, message.getCategory())
                .stream()
                // filter out user who created the notification
                .filter(user -> !message.getUserName().equals(user.getName()))
                .forEach(user -> sendPushNotification(user.getId(), message));
    }

    /**
     * Send push notification.
     * 
     * @param id      mail account to send the email
     * @param message message to send in the email
     */
    private void sendPushNotification(String id, NotificationMessage message) {
        /*
         * simulate send push notification message.
         */
        log.info("push notification sent to {} with message {}", id, message.getMessage());
    }
}

package com.sms.sms.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.model.NotificationChannel;
import com.sms.sms.service.NotificationService;
import com.sms.sms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
     * Send sms notification.
     * @param phoneNumber phone number to send the sms
     * @param message message to send in the sms
     */
    private void sendPushNotification(String phoneNumber, NotificationMessage message) {
        /*
         * simulate send sms notification message.
         */
        log.info("sms notification sent to {} with message {}", phoneNumber, message.getMessage());
    }
}

package com.sms.sms.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.model.NotificationChannel;
import com.sms.sms.service.NotificationService;
import com.sms.sms.service.UserService;
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
        log.info("Sending sms notification from user '{}': [{}] {}",
                message.getUserName(), message.getCategory(), message.getMessage());

        userService.getUsersByChannelAndCategory(NotificationChannel.SMS, message.getCategory())
                .stream()
                // filter out user who created the notification
                .filter(user -> !message.getUserName().equals(user.getName()))
                .forEach(user -> sendSmsNotification(user.getPhoneNumber(), message));
    }

    /**
     * Send sms notification.
     * 
     * @param phoneNumber phone number to send the sms
     * @param message     message to send in the sms
     */
    private void sendSmsNotification(String phoneNumber, NotificationMessage message) {
        /*
         * simulate send sms notification message.
         */
        log.info("sms notification sent to {} with message {}", phoneNumber, message.getMessage());
    }
}

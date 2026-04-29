package com.email.notification.service.impl;

import com.email.notification.service.NotificationService;
import com.email.notification.service.UserService;
import com.notification.api.dto.NotificationMessage;
import com.notification.api.model.NotificationChannel;
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
        log.info("Sending email notification from user '{}': [{}] {}",
                message.getUserName(), message.getCategory(), message.getMessage());

        userService.getUsersByChannelAndCategory(NotificationChannel.EMAIL, message.getCategory())
                .stream()
                // filter out user who created the notification
                .filter(user -> !message.getUserName().equals(user.getName()))
                .forEach(user -> sendEmail(user.getEmail(), message));
    }

    /**
     * Send email notification.
     * 
     * @param email   mail account to send the email
     * @param message message to send in the email
     */
    private void sendEmail(String email, NotificationMessage message) {
        /**
         * simulate send email message.
         */
        log.info("email sent to {} with message {}", email, message.getMessage());
    }
}

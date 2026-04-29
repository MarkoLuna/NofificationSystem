package com.notification.notification.service;

import com.notification.notification.dto.NotificationRequest;
import com.notification.api.model.NotificationChannel;

/**
 * Interface for notification strategies.
 */
public interface NotificationStrategy {

    /**
     * Sends the notification to the user.
     * 
     * @param request The notification request containing user, category, and
     *                message details.
     * @param userName    The user name who publishes notification.
     */
    void send(NotificationRequest request, String userName);

    /**
     * Checks if the strategy supports the given channel.
     * 
     * @param channel The channel to check.
     * @return True if the strategy supports the channel, false otherwise.
     */
    boolean supports(NotificationChannel channel);
}

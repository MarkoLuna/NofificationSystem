package com.sms.sms.service;

import com.notification.api.dto.NotificationMessage;

/**
 * Service for processing notification messages.
 */
public interface NotificationService {

    /**
     * Process notification message.
     * 
     * @param message message to process
     */
    void processNotificationMessage(NotificationMessage message);
}

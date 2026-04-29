package com.notification.notification.service;

import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.dto.NotificationRequest;
import com.notification.notification.entities.Notification;
import com.notification.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationStrategy> strategies;
    private final NotificationRepository notificationRepository;

    /**
     * Process notification request.
     * 
     * @param request  request to process
     * @param userName user name
     */
    public void process(NotificationRequest request, String userName) {
        log.trace("Processing notification request for channel: {}", request.getChannel());

        Notification notification = Notification.builder()
                .creator(userName)
                .message(request.getMessage())
                .channel(request.getChannel())
                .category(request.getCategory())
                .build();

        notificationRepository.save(notification);

        strategies.stream()
                .filter(strategy -> strategy.supports(request.getChannel()))
                .findFirst()
                .ifPresentOrElse(
                        strategy -> strategy.send(request, userName),
                        () -> log.error("No strategy found for channel: {}", request.getChannel()));
    }

    /**
     * Find notifications by creator.
     * 
     * @param creator creator to find notifications for
     * @return list of notifications for the creator
     */
    public List<Notification> findByCreator(String creator) {
        return notificationRepository.findByCreator(creator);
    }

    /**
     * Find notifications by channel and category.
     * 
     * @param channel  channel to find notifications for
     * @param category category to find notifications for
     * @return list of notifications for the channel and category
     */
    public List<Notification> findByChannelAndCategory(NotificationChannel channel, NotificationCategory category) {
        return notificationRepository.findByChannelAndCategory(channel, category);
    }
}

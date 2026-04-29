package com.notification.notification.repositories;

import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.entities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

  List<Notification> findByCreator(String creator);

  List<Notification> findByChannelAndCategory(NotificationChannel channel, NotificationCategory category);
}

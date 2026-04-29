package com.notification.notification.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@Document(collection = "notifications")
@CompoundIndexes({
        @CompoundIndex(name = "idx_channel_category", def = "{channel: 1, category: 1}"),
        @CompoundIndex(name = "idx_creator_id", def = "{creator: 1, id: 1}")
})
public class Notification {
    @Id
    private String id;
    private NotificationChannel channel;
    private NotificationCategory category;
    private String message;
    private String creator;
}

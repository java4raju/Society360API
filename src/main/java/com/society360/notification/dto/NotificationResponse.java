package com.society360.notification.dto;

import com.society360.notification.entity.Notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
    UUID id, String title, String body, String type,
    boolean read, String link, Instant createdAt
) {
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(n.getId(), n.getTitle(), n.getBody(),
            n.getType(), n.isRead(), n.getLink(), n.getCreatedAt());
    }
}

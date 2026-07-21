package com.society360.notice.dto;

import com.society360.notice.entity.Notice;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record NoticeResponse(
    UUID id, String title, String body, String category,
    String author, LocalDate date, boolean pinned, boolean important,
    Instant createdAt, Instant updatedAt
) {
    public static NoticeResponse from(Notice n) {
        return new NoticeResponse(n.getId(), n.getTitle(), n.getBody(), n.getCategory(),
            n.getAuthor(), n.getDate(), n.isPinned(), n.isImportant(), n.getCreatedAt(), n.getUpdatedAt());
    }
}

package com.society360.meeting.dto;

import com.society360.meeting.entity.Meeting;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MeetingResponse(
    UUID id,
    String title,
    String type,
    String status,
    LocalDate date,
    String location,
    int attendees,
    List<String> agenda,
    List<String> decisions,
    String minutesFilePath,
    Instant createdAt,
    Instant updatedAt
) {
    public static MeetingResponse from(Meeting m) {
        return new MeetingResponse(
            m.getId(), m.getTitle(), m.getType(), m.getStatus(), m.getDate(),
            m.getLocation(), m.getAttendees(), m.getAgenda(), m.getDecisions(),
            m.getMinutesFilePath(), m.getCreatedAt(), m.getUpdatedAt()
        );
    }
}

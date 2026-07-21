package com.society360.poll.dto;

import com.society360.poll.entity.Poll;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PollResponse(
    UUID id,
    String title,
    String description,
    String category,
    String status,
    LocalDate endDate,
    int totalVotes,
    List<Poll.PollOption> options,
    boolean hasVoted,
    Instant createdAt,
    Instant updatedAt
) {
    public static PollResponse from(Poll p, boolean hasVoted) {
        return new PollResponse(
            p.getId(), p.getTitle(), p.getDescription(), p.getCategory(),
            p.getStatus(), p.getEndDate(), p.getTotalVotes(), p.getOptions(),
            hasVoted, p.getCreatedAt(), p.getUpdatedAt()
        );
    }
}

package com.society360.complaint.dto;

import com.society360.complaint.entity.Complaint;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ComplaintResponse(
    UUID id,
    String title,
    String description,
    String category,
    String priority,
    String status,
    String resident,
    String flatNumber,
    String assignedTo,
    LocalDate createdDate,
    LocalDate resolvedDate,
    Instant createdAt,
    Instant updatedAt
) {
    public static ComplaintResponse from(Complaint c) {
        return new ComplaintResponse(
            c.getId(), c.getTitle(), c.getDescription(), c.getCategory(),
            c.getPriority(), c.getStatus(), c.getResident(), c.getFlatNumber(),
            c.getAssignedTo(), c.getCreatedDate(), c.getResolvedDate(),
            c.getCreatedAt(), c.getUpdatedAt()
        );
    }
}

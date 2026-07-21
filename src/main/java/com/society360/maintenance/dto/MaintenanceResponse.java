package com.society360.maintenance.dto;

import com.society360.maintenance.entity.MaintenanceRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MaintenanceResponse(
    UUID id, String title, String description, String category,
    String priority, String status, String resident, String flatNumber,
    String assignedTo, LocalDate scheduledDate, LocalDate resolvedDate,
    Instant createdAt, Instant updatedAt
) {
    public static MaintenanceResponse from(MaintenanceRequest m) {
        return new MaintenanceResponse(m.getId(), m.getTitle(), m.getDescription(),
            m.getCategory(), m.getPriority(), m.getStatus(), m.getResident(),
            m.getFlatNumber(), m.getAssignedTo(), m.getScheduledDate(),
            m.getResolvedDate(), m.getCreatedAt(), m.getUpdatedAt());
    }
}

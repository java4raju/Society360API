package com.society360.project.dto;

import com.society360.project.entity.Project;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectResponse(
    UUID id, String name, String description, String category, String status,
    BigDecimal budget, BigDecimal spent, int progress,
    String owner, LocalDate startDate, LocalDate endDate,
    Instant createdAt, Instant updatedAt
) {
    public static ProjectResponse from(Project p) {
        return new ProjectResponse(p.getId(), p.getName(), p.getDescription(), p.getCategory(),
            p.getStatus(), p.getBudget(), p.getSpent(), p.getProgress(),
            p.getOwner(), p.getStartDate(), p.getEndDate(), p.getCreatedAt(), p.getUpdatedAt());
    }
}

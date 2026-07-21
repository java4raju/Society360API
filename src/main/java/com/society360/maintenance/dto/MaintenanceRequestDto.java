package com.society360.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MaintenanceRequestDto(
    @NotBlank String title,
    String description,
    @NotBlank String category,
    @NotBlank String priority,
    @NotBlank String status,
    @NotBlank String resident,
    @NotBlank String flatNumber,
    String assignedTo,
    LocalDate scheduledDate,
    LocalDate resolvedDate
) {}

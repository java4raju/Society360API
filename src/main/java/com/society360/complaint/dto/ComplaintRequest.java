package com.society360.complaint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ComplaintRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank String category,
    @NotBlank String priority,
    @NotBlank String status,
    @NotBlank String resident,
    @NotBlank String flatNumber,
    String assignedTo,
    @NotNull LocalDate createdDate,
    LocalDate resolvedDate
) {}

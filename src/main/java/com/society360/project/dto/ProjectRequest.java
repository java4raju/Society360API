package com.society360.project.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectRequest(
    @NotBlank String name,
    String description,
    @NotBlank String category,
    @NotBlank String status,
    @DecimalMin("0") BigDecimal budget,
    @DecimalMin("0") BigDecimal spent,
    @Min(0) @Max(100) int progress,
    String owner,
    @NotNull LocalDate startDate,
    LocalDate endDate
) {}

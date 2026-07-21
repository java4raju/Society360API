package com.society360.vendor.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VendorRequest(
    @NotBlank String name,
    @NotBlank String category,
    @NotBlank String status,
    @NotBlank String contact,
    @Email String email,
    @DecimalMin("0") BigDecimal contractValue,
    @Min(1) @Max(5) int rating,
    LocalDate contractStart,
    LocalDate contractEnd
) {}

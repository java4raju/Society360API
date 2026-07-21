package com.society360.resident.dto;

import com.society360.resident.entity.Resident;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResidentRequest(
    @NotBlank @Size(max = 10) String block,
    @NotBlank @Size(max = 20) String flatNumber,
    @NotBlank String ownerName,
    String tenantName,
    @NotBlank @Size(max = 20) String contact,
    @Email @NotBlank String email,
    @Min(0) @Max(10) int parkingSlots,
    @NotNull Resident.Occupancy occupancy,
    @NotNull Resident.Status status,
    @DecimalMin("0") BigDecimal duesAmount,
    LocalDate joinedDate
) {}

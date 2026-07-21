package com.society360.vendor.dto;

import com.society360.vendor.entity.Vendor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record VendorResponse(
    UUID id, String name, String category, String status,
    String contact, String email, BigDecimal contractValue, int rating,
    LocalDate contractStart, LocalDate contractEnd,
    Instant createdAt, Instant updatedAt
) {
    public static VendorResponse from(Vendor v) {
        return new VendorResponse(v.getId(), v.getName(), v.getCategory(), v.getStatus(),
            v.getContact(), v.getEmail(), v.getContractValue(), v.getRating(),
            v.getContractStart(), v.getContractEnd(), v.getCreatedAt(), v.getUpdatedAt());
    }
}

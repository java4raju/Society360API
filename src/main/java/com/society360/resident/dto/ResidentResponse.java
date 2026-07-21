package com.society360.resident.dto;

import com.society360.resident.entity.Resident;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ResidentResponse(
    UUID id,
    String block,
    String flatNumber,
    String ownerName,
    String tenantName,
    String contact,
    String email,
    int parkingSlots,
    Resident.Occupancy occupancy,
    Resident.Status status,
    BigDecimal duesAmount,
    LocalDate joinedDate,
    Instant createdAt,
    Instant updatedAt
) {
    public static ResidentResponse from(Resident r) {
        return new ResidentResponse(
            r.getId(), r.getBlock(), r.getFlatNumber(),
            r.getOwnerName(), r.getTenantName(), r.getContact(), r.getEmail(),
            r.getParkingSlots(), r.getOccupancy(), r.getStatus(),
            r.getDuesAmount(), r.getJoinedDate(), r.getCreatedAt(), r.getUpdatedAt()
        );
    }
}

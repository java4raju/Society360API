package com.society360.tenant.dto;

import com.society360.tenant.entity.Tenant;
import java.time.Instant;
import java.util.UUID;

public record TenantResponse(
    UUID id,
    String name,
    String slug,
    String status,
    String contactName,
    String contactEmail,
    String city,
    int maxUnits,
    Instant trialEndsAt,
    Instant createdAt
) {
    public static TenantResponse from(Tenant t) {
        return new TenantResponse(t.getId(), t.getName(), t.getSlug(),
            t.getStatus().name(), t.getContactName(), t.getContactEmail(),
            t.getCity(), t.getMaxUnits(), t.getTrialEndsAt(), t.getCreatedAt());
    }
}

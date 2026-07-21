package com.society360.tenant.dto;

import java.util.UUID;

public record RegistrationResponse(
    UUID tenantId,
    String slug,
    String subdomainUrl,
    String message
) {}

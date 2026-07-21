package com.society360.tenant.dto;

import jakarta.validation.constraints.*;

public record RegistrationRequest(
    @NotBlank String societyName,
    @NotBlank @Pattern(regexp = "^[a-z0-9-]{3,50}$", message = "Slug must be 3-50 chars: lowercase letters, digits, hyphens only") String slug,
    String rwaRegNumber,
    String address,
    @NotBlank String city,
    @Pattern(regexp = "^[1-9][0-9]{5}$") String pincode,
    @NotBlank String adminName,
    @Email @NotBlank String adminEmail,
    String adminPhone,
    @NotBlank @Size(min = 8) String adminPassword,
    @Min(1) @Max(5000) int unitCount
) {}

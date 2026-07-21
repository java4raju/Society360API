package com.society360.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record BroadcastRequest(
    @NotBlank String title,
    @NotBlank String body,
    String type,
    String link
) {}

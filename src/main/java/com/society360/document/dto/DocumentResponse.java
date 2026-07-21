package com.society360.document.dto;

import com.society360.document.entity.Document;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
    UUID id, String name, String fileName, String mimeType,
    long sizeBytes, String category, String uploadedBy,
    String downloadUrl, Instant createdAt
) {
    public static DocumentResponse from(Document d) {
        return new DocumentResponse(d.getId(), d.getName(), d.getFileName(),
            d.getMimeType(), d.getSizeBytes(), d.getCategory(), d.getUploadedBy(),
            "/api/v1/documents/" + d.getId() + "/download", d.getCreatedAt());
    }
}

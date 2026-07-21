package com.society360.document.entity;

import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Document extends BaseEntity {

    @Column(nullable = false) private String name;
    @Column(name = "file_name", nullable = false) private String fileName;
    @Column(name = "file_path", nullable = false, length = 1000) private String filePath;
    @Column(name = "mime_type", nullable = false, length = 100) private String mimeType;
    @Column(name = "size_bytes", nullable = false) private long sizeBytes;
    @Column(nullable = false) private String category;
    @Column(name = "uploaded_by", nullable = false) private String uploadedBy;
}

package com.society360.complaint.entity;

import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "complaints")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Complaint extends BaseEntity {

    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false) private String category;
    @Column(nullable = false) @Builder.Default private String priority = "Medium";
    @Column(nullable = false) @Builder.Default private String status = "Open";
    @Column(nullable = false) private String resident;
    @Column(name = "flat_number", nullable = false) private String flatNumber;
    @Column(name = "assigned_to") private String assignedTo;
    @Column(name = "created_date") private LocalDate createdDate;
    @Column(name = "resolved_date") private LocalDate resolvedDate;
}

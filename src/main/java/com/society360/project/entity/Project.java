package com.society360.project.entity;

import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project extends BaseEntity {

    @Column(nullable = false) private String name;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false) private String category;
    @Column(nullable = false) @Builder.Default private String status = "Proposed";
    @Column(precision = 14, scale = 2) @Builder.Default private BigDecimal budget = BigDecimal.ZERO;
    @Column(precision = 14, scale = 2) @Builder.Default private BigDecimal spent = BigDecimal.ZERO;
    @Column(nullable = false) @Builder.Default private int progress = 0;
    private String owner;
    @Column(name = "start_date") private LocalDate startDate;
    @Column(name = "end_date") private LocalDate endDate;
}

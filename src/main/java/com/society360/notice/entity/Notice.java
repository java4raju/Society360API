package com.society360.notice.entity;

import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "notices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notice extends BaseEntity {

    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT", nullable = false) private String body;
    @Column(nullable = false) private String category;
    @Column(nullable = false) private String author;
    @Column(nullable = false) private LocalDate date;
    @Builder.Default private boolean pinned = false;
    @Builder.Default private boolean important = false;
}

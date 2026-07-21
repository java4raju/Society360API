package com.society360.notification.entity;

import com.society360.auth.entity.User;
import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications",
    indexes = @Index(name = "idx_notif_user", columnList = "user_id, read"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // null = broadcast to all

    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT", nullable = false) private String body;
    @Column(nullable = false) @Builder.Default private String type = "info";
    @Column(nullable = false) @Builder.Default private boolean read = false;
    private String link;
}

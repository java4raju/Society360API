package com.society360.poll.entity;

import com.society360.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polls")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Poll extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    @Builder.Default
    private String status = "Active";

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_votes", nullable = false)
    @Builder.Default
    private int totalVotes = 0;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private List<PollOption> options = new ArrayList<>();

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PollOption {
        private String id;
        private String label;
        private int votes;
    }
}

package com.society360.meeting.entity;

import com.society360.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meetings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Meeting extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Builder.Default
    private String type = "Committee";

    @Column(nullable = false)
    @Builder.Default
    private String status = "Scheduled";

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    @Builder.Default
    private int attendees = 0;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private List<String> agenda = new ArrayList<>();

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private List<String> decisions = new ArrayList<>();

    @Column(name = "minutes_file_path")
    private String minutesFilePath;
}

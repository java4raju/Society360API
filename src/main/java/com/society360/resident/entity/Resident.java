package com.society360.resident.entity;

import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "residents", uniqueConstraints = @UniqueConstraint(columnNames = {"block", "flat_number"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resident extends BaseEntity {

    @Column(nullable = false, length = 10)
    private String block;

    @Column(name = "flat_number", nullable = false, length = 20)
    private String flatNumber;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(nullable = false, length = 20)
    private String contact;

    @Column(nullable = false)
    private String email;

    @Column(name = "parking_slots")
    @Builder.Default
    private int parkingSlots = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Occupancy occupancy = Occupancy.OWNER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "dues_amount", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal duesAmount = BigDecimal.ZERO;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    public enum Occupancy { OWNER, TENANT, VACANT }
    public enum Status { ACTIVE, INACTIVE }
}

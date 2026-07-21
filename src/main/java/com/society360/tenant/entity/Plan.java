package com.society360.tenant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "plans")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "price_monthly", nullable = false)
    @Builder.Default
    private BigDecimal priceMonthly = BigDecimal.ZERO;

    @Column(name = "price_yearly", nullable = false)
    @Builder.Default
    private BigDecimal priceYearly = BigDecimal.ZERO;

    @Column(name = "max_units", nullable = false)
    private int maxUnits;

    @Column(name = "max_staff", nullable = false)
    @Builder.Default
    private int maxStaff = 5;

    @Column(name = "razorpay_plan_id")
    private String razorpayPlanId;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;
}

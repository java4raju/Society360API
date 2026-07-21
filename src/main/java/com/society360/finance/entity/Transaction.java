package com.society360.finance.entity;

import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_txn_date", columnList = "date"),
    @Index(name = "idx_txn_type", columnList = "type"),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.COMPLETED;

    @Column(nullable = false)
    @Builder.Default
    private String method = "Bank Transfer";

    // Populated when transaction is synced from bank
    @Column(name = "bank_ref_id", unique = true)
    private String bankRefId;

    @Column(name = "bank_synced")
    @Builder.Default
    private boolean bankSynced = false;

    public enum Type { INCOME, EXPENSE }
    public enum Status { COMPLETED, PENDING, FAILED }
}

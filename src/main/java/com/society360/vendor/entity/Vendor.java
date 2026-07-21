package com.society360.vendor.entity;

import com.society360.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vendors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vendor extends BaseEntity {

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String category;
    @Column(nullable = false) @Builder.Default private String status = "Active";
    @Column(nullable = false, length = 20) private String contact;
    private String email;
    @Column(name = "contract_value", precision = 14, scale = 2) @Builder.Default private BigDecimal contractValue = BigDecimal.ZERO;
    @Column(nullable = false) @Builder.Default private int rating = 3;
    @Column(name = "contract_start") private LocalDate contractStart;
    @Column(name = "contract_end") private LocalDate contractEnd;
}

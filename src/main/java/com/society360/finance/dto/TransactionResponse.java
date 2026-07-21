package com.society360.finance.dto;

import com.society360.finance.entity.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(
    UUID id,
    LocalDate date,
    String description,
    String category,
    BigDecimal amount,
    Transaction.Type type,
    Transaction.Status status,
    String method,
    boolean bankSynced,
    Instant createdAt
) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(
            t.getId(), t.getDate(), t.getDescription(), t.getCategory(),
            t.getAmount(), t.getType(), t.getStatus(), t.getMethod(),
            t.isBankSynced(), t.getCreatedAt()
        );
    }
}

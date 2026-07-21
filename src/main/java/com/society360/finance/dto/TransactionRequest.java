package com.society360.finance.dto;

import com.society360.finance.entity.Transaction;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
    @NotNull LocalDate date,
    @NotBlank String description,
    @NotBlank String category,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotNull Transaction.Type type,
    @NotNull Transaction.Status status,
    @NotBlank String method
) {}

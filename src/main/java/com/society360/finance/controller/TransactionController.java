package com.society360.finance.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.common.dto.PageResponse;
import com.society360.finance.dto.TransactionRequest;
import com.society360.finance.dto.TransactionResponse;
import com.society360.finance.entity.Transaction;
import com.society360.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Finance / Transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Search transactions with filters and pagination")
    public ResponseEntity<ApiResponse<PageResponse<TransactionResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Transaction.Type type,
            @RequestParam(required = false) Transaction.Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(transactionService.search(q, type, status, from, to, page, size)));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get total income, expense and balance")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> summary() {
        return ResponseEntity.ok(ApiResponse.ok(transactionService.summary()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(transactionService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionResponse>> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Transaction created", transactionService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionResponse>> update(@PathVariable UUID id,
                                                                   @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Transaction updated", transactionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        transactionService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Transaction deleted"));
    }

    @PostMapping("/bulk-import")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bulk import transactions")
    public ResponseEntity<ApiResponse<String>> bulkImport(@Valid @RequestBody List<TransactionRequest> requests) {
        int count = transactionService.bulkImport(requests);
        return ResponseEntity.ok(ApiResponse.ok(count + " transactions imported successfully"));
    }
}

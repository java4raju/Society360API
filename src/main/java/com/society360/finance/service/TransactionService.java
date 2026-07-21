package com.society360.finance.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.finance.dto.TransactionRequest;
import com.society360.finance.dto.TransactionResponse;
import com.society360.finance.entity.Transaction;
import com.society360.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public PageResponse<TransactionResponse> search(String q, Transaction.Type type, Transaction.Status status,
                                                     LocalDate from, LocalDate to, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        return PageResponse.of(transactionRepository.search(q, type, status, from, to, pageable)
            .map(TransactionResponse::from));
    }

    public TransactionResponse getById(UUID id) {
        return TransactionResponse.from(findOrThrow(id));
    }

    public Map<String, BigDecimal> summary() {
        BigDecimal income = transactionRepository.sumByType(Transaction.Type.INCOME);
        BigDecimal expense = transactionRepository.sumByType(Transaction.Type.EXPENSE);
        return Map.of(
            "totalIncome", income,
            "totalExpense", expense,
            "balance", income.subtract(expense)
        );
    }

    @Transactional
    public TransactionResponse create(TransactionRequest req) {
        return TransactionResponse.from(transactionRepository.save(toEntity(req)));
    }

    @Transactional
    public TransactionResponse update(UUID id, TransactionRequest req) {
        Transaction txn = findOrThrow(id);
        txn.setDate(req.date());
        txn.setDescription(req.description());
        txn.setCategory(req.category());
        txn.setAmount(req.amount());
        txn.setType(req.type());
        txn.setStatus(req.status());
        txn.setMethod(req.method());
        return TransactionResponse.from(transactionRepository.save(txn));
    }

    @Transactional
    public void delete(UUID id) {
        transactionRepository.delete(findOrThrow(id));
    }

    @Transactional
    public int bulkImport(List<TransactionRequest> requests) {
        List<Transaction> entities = requests.stream().map(this::toEntity).toList();
        transactionRepository.saveAll(entities);
        return entities.size();
    }

    private Transaction toEntity(TransactionRequest req) {
        return Transaction.builder()
            .date(req.date())
            .description(req.description())
            .category(req.category())
            .amount(req.amount())
            .type(req.type())
            .status(req.status())
            .method(req.method())
            .build();
    }

    private Transaction findOrThrow(UUID id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
    }
}

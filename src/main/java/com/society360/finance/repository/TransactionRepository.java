package com.society360.finance.repository;

import com.society360.finance.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("""
        SELECT t FROM Transaction t
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(t.category) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:type IS NULL OR t.type = :type)
          AND (:status IS NULL OR t.status = :status)
          AND (:from IS NULL OR t.date >= :from)
          AND (:to IS NULL OR t.date <= :to)
        """)
    Page<Transaction> search(@Param("search") String search,
                             @Param("type") Transaction.Type type,
                             @Param("status") Transaction.Status status,
                             @Param("from") LocalDate from,
                             @Param("to") LocalDate to,
                             Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type AND t.status = 'COMPLETED'")
    BigDecimal sumByType(@Param("type") Transaction.Type type);

    @Query("""
        SELECT FUNCTION('DATE_TRUNC', 'month', t.date) AS month,
               t.type,
               SUM(t.amount) AS total
        FROM Transaction t
        WHERE t.date >= :from AND t.status = 'COMPLETED'
        GROUP BY FUNCTION('DATE_TRUNC', 'month', t.date), t.type
        ORDER BY 1
        """)
    java.util.List<Object[]> monthlyTrend(@Param("from") LocalDate from);

    boolean existsByBankRefId(String bankRefId);
}

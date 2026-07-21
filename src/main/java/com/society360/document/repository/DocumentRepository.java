package com.society360.document.repository;

import com.society360.document.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    @Query("""
        SELECT d FROM Document d
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(d.category) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:category IS NULL OR :category = '' OR d.category = :category)
        """)
    Page<Document> search(@Param("search") String search,
                          @Param("category") String category,
                          Pageable pageable);
}

package com.society360.notice.repository;

import com.society360.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID> {

    @Query("""
        SELECT n FROM Notice n
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(n.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(n.category) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:category IS NULL OR :category = '' OR n.category = :category)
        ORDER BY n.pinned DESC, n.date DESC
        """)
    Page<Notice> search(@Param("search") String search,
                        @Param("category") String category,
                        Pageable pageable);
}

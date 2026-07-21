package com.society360.complaint.repository;

import com.society360.complaint.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

    @Query("""
        SELECT c FROM Complaint c
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(c.resident) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR c.status = :status)
          AND (:priority IS NULL OR c.priority = :priority)
        """)
    Page<Complaint> search(@Param("search") String search,
                           @Param("status") String status,
                           @Param("priority") String priority,
                           Pageable pageable);

    long countByStatus(String status);

    @Query("SELECT c.category, COUNT(c) FROM Complaint c GROUP BY c.category")
    List<Object[]> countByCategory();
}

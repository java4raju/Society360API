package com.society360.maintenance.repository;

import com.society360.maintenance.entity.MaintenanceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MaintenanceRepository extends JpaRepository<MaintenanceRequest, UUID> {

    @Query("""
        SELECT m FROM MaintenanceRequest m
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(m.resident) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(m.flatNumber) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR :status = '' OR m.status = :status)
          AND (:priority IS NULL OR :priority = '' OR m.priority = :priority)
        """)
    Page<MaintenanceRequest> search(@Param("search") String search,
                                    @Param("status") String status,
                                    @Param("priority") String priority,
                                    Pageable pageable);
}

package com.society360.vendor.repository;

import com.society360.vendor.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    @Query("""
        SELECT v FROM Vendor v
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(v.name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(v.category) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR :status = '' OR v.status = :status)
        """)
    Page<Vendor> search(@Param("search") String search,
                        @Param("status") String status,
                        Pageable pageable);
}

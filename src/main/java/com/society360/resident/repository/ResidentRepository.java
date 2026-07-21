package com.society360.resident.repository;

import com.society360.resident.entity.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, UUID> {

    boolean existsByBlockAndFlatNumber(String block, String flatNumber);

    long countByStatus(Resident.Status status);

    @Query("SELECT r.occupancy, COUNT(r) FROM Resident r GROUP BY r.occupancy")
    List<Object[]> countByOccupancy();

    @Query("""
        SELECT r FROM Resident r
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(r.ownerName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(r.flatNumber) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(r.block) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(r.email) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR r.status = :status)
          AND (:occupancy IS NULL OR r.occupancy = :occupancy)
        """)
    Page<Resident> search(@Param("search") String search,
                          @Param("status") Resident.Status status,
                          @Param("occupancy") Resident.Occupancy occupancy,
                          Pageable pageable);
}

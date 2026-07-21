package com.society360.meeting.repository;

import com.society360.meeting.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {

    @Query("""
        SELECT m FROM Meeting m
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(m.location) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR :status = '' OR m.status = :status)
        """)
    Page<Meeting> search(@Param("search") String search,
                         @Param("status") String status,
                         Pageable pageable);
}

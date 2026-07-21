package com.society360.poll.repository;

import com.society360.poll.entity.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PollRepository extends JpaRepository<Poll, UUID> {

    @Query("""
        SELECT p FROM Poll p
        WHERE (:search IS NULL OR :search = ''
               OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:status IS NULL OR :status = '' OR p.status = :status)
        """)
    Page<Poll> search(@Param("search") String search,
                      @Param("status") String status,
                      Pageable pageable);
}

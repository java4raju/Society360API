package com.society360.poll.repository;

import com.society360.poll.entity.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PollVoteRepository extends JpaRepository<PollVote, UUID> {
    boolean existsByPollIdAndUserId(UUID pollId, UUID userId);
    Optional<PollVote> findByPollIdAndUserId(UUID pollId, UUID userId);
}

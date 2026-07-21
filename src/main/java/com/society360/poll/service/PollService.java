package com.society360.poll.service;

import com.society360.auth.entity.User;
import com.society360.auth.repository.UserRepository;
import com.society360.common.dto.PageResponse;
import com.society360.common.exception.BusinessException;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.poll.dto.PollRequest;
import com.society360.poll.dto.PollResponse;
import com.society360.poll.entity.Poll;
import com.society360.poll.entity.PollVote;
import com.society360.poll.repository.PollRepository;
import com.society360.poll.repository.PollVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PollService {

    private final PollRepository pollRepository;
    private final PollVoteRepository pollVoteRepository;
    private final UserRepository userRepository;

    public PageResponse<PollResponse> search(String q, String status, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        UUID currentUserId = currentUser().getId();
        return PageResponse.of(pollRepository.search(q, status, pageable)
            .map(p -> PollResponse.from(p, pollVoteRepository.existsByPollIdAndUserId(p.getId(), currentUserId))));
    }

    public PollResponse getById(UUID id) {
        Poll poll = findOrThrow(id);
        UUID currentUserId = currentUser().getId();
        return PollResponse.from(poll, pollVoteRepository.existsByPollIdAndUserId(id, currentUserId));
    }

    @Transactional
    public PollResponse create(PollRequest req) {
        List<Poll.PollOption> options = buildOptions(req.optionLabels(), new ArrayList<>());
        Poll poll = Poll.builder()
            .title(req.title())
            .description(req.description())
            .category(req.category())
            .status(req.status())
            .endDate(req.endDate())
            .options(options)
            .build();
        return PollResponse.from(pollRepository.save(poll), false);
    }

    @Transactional
    public PollResponse update(UUID id, PollRequest req) {
        Poll poll = findOrThrow(id);
        poll.setTitle(req.title());
        poll.setDescription(req.description());
        poll.setCategory(req.category());
        poll.setStatus(req.status());
        poll.setEndDate(req.endDate());
        // Preserve existing vote counts when option count/order matches
        List<Poll.PollOption> updatedOptions = buildOptions(req.optionLabels(), poll.getOptions());
        poll.setOptions(updatedOptions);
        poll.setTotalVotes(updatedOptions.stream().mapToInt(Poll.PollOption::getVotes).sum());
        UUID currentUserId = currentUser().getId();
        return PollResponse.from(pollRepository.save(poll),
            pollVoteRepository.existsByPollIdAndUserId(id, currentUserId));
    }

    @Transactional
    public PollResponse vote(UUID pollId, String optionId) {
        Poll poll = findOrThrow(pollId);
        User user = currentUser();

        if (!"Active".equals(poll.getStatus())) {
            throw new BusinessException("This poll is not active");
        }
        if (poll.getEndDate().isBefore(LocalDate.now())) {
            throw new BusinessException("This poll has ended");
        }
        if (pollVoteRepository.existsByPollIdAndUserId(pollId, user.getId())) {
            throw new BusinessException("You have already voted in this poll");
        }

        Poll.PollOption target = poll.getOptions().stream()
            .filter(o -> o.getId().equals(optionId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Poll option", optionId));

        target.setVotes(target.getVotes() + 1);
        poll.setTotalVotes(poll.getTotalVotes() + 1);

        PollVote vote = PollVote.builder()
            .poll(poll)
            .optionId(optionId)
            .user(user)
            .build();
        pollVoteRepository.save(vote);
        pollRepository.save(poll);

        return PollResponse.from(poll, true);
    }

    @Transactional
    public void delete(UUID id) {
        pollRepository.delete(findOrThrow(id));
    }

    private List<Poll.PollOption> buildOptions(List<String> labels, List<Poll.PollOption> existing) {
        List<Poll.PollOption> result = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            int existingVotes = (i < existing.size()) ? existing.get(i).getVotes() : 0;
            String existingId = (i < existing.size()) ? existing.get(i).getId() : UUID.randomUUID().toString();
            result.add(new Poll.PollOption(existingId, labels.get(i), existingVotes));
        }
        return result;
    }

    private Poll findOrThrow(UUID id) {
        return pollRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Poll", id));
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", email));
    }
}

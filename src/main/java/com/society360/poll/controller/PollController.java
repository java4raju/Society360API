package com.society360.poll.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.common.dto.PageResponse;
import com.society360.poll.dto.PollRequest;
import com.society360.poll.dto.PollResponse;
import com.society360.poll.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/polls")
@RequiredArgsConstructor
@Tag(name = "Polls")
public class PollController {

    private final PollService pollService;

    @GetMapping
    @Operation(summary = "List polls — includes hasVoted flag for current user")
    public ResponseEntity<ApiResponse<PageResponse<PollResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(pollService.search(q, status, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PollResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(pollService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PollResponse>> create(@Valid @RequestBody PollRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Poll created", pollService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PollResponse>> update(@PathVariable UUID id,
                                                            @Valid @RequestBody PollRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Poll updated", pollService.update(id, request)));
    }

    @PostMapping("/{id}/vote")
    @Operation(summary = "Cast a vote — one per resident per poll, enforced at DB level")
    public ResponseEntity<ApiResponse<PollResponse>> vote(@PathVariable UUID id,
                                                          @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok("Vote recorded", pollService.vote(id, body.get("optionId"))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        pollService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Poll deleted"));
    }
}

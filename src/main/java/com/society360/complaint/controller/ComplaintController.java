package com.society360.complaint.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.common.dto.PageResponse;
import com.society360.complaint.dto.ComplaintRequest;
import com.society360.complaint.dto.ComplaintResponse;
import com.society360.complaint.service.ComplaintService;
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
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
@Tag(name = "Complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    @GetMapping
    @Operation(summary = "Search complaints with filters and pagination")
    public ResponseEntity<ApiResponse<PageResponse<ComplaintResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(complaintService.search(q, status, priority, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplaintResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(complaintService.getById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<ComplaintResponse>> create(@Valid @RequestBody ComplaintRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Complaint created", complaintService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<ComplaintResponse>> update(@PathVariable UUID id,
                                                                 @Valid @RequestBody ComplaintRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Complaint updated", complaintService.update(id, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @Operation(summary = "Quick status update — Open / Assigned / In Progress / Resolved / Closed")
    public ResponseEntity<ApiResponse<ComplaintResponse>> updateStatus(@PathVariable UUID id,
                                                                       @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated", complaintService.updateStatus(id, body.get("status"))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        complaintService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Complaint deleted"));
    }
}

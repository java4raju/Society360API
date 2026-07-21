package com.society360.resident.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.common.dto.PageResponse;
import com.society360.resident.dto.ResidentRequest;
import com.society360.resident.dto.ResidentResponse;
import com.society360.resident.entity.Resident;
import com.society360.resident.service.ResidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/residents")
@RequiredArgsConstructor
@Tag(name = "Residents")
public class ResidentController {

    private final ResidentService residentService;

    @GetMapping
    @Operation(summary = "Search and list residents with pagination")
    public ResponseEntity<ApiResponse<PageResponse<ResidentResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Resident.Status status,
            @RequestParam(required = false) Resident.Occupancy occupancy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(residentService.search(q, status, occupancy, page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resident by ID")
    public ResponseEntity<ApiResponse<ResidentResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(residentService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new resident")
    public ResponseEntity<ApiResponse<ResidentResponse>> create(@Valid @RequestBody ResidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Resident created", residentService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a resident")
    public ResponseEntity<ApiResponse<ResidentResponse>> update(@PathVariable UUID id,
                                                                @Valid @RequestBody ResidentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Resident updated", residentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a resident")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        residentService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Resident deleted"));
    }

    @PostMapping("/bulk-import")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bulk import residents from JSON array")
    public ResponseEntity<ApiResponse<String>> bulkImport(@Valid @RequestBody List<ResidentRequest> requests) {
        int count = residentService.bulkImport(requests);
        return ResponseEntity.ok(ApiResponse.ok(count + " residents imported successfully"));
    }
}

package com.society360.maintenance.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.common.dto.PageResponse;
import com.society360.maintenance.dto.MaintenanceRequestDto;
import com.society360.maintenance.dto.MaintenanceResponse;
import com.society360.maintenance.service.MaintenanceService;
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
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
@Tag(name = "Maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MaintenanceResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(maintenanceService.search(q, status, priority, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(maintenanceService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MaintenanceResponse>> create(@Valid @RequestBody MaintenanceRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Maintenance request created", maintenanceService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> update(@PathVariable UUID id,
                                                                   @Valid @RequestBody MaintenanceRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok("Request updated", maintenanceService.update(id, request)));
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @Operation(summary = "Assign technician — auto-sets status to Assigned")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> assign(@PathVariable UUID id,
                                                                   @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok("Assigned", maintenanceService.assign(id, body.get("assignedTo"))));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> updateStatus(@PathVariable UUID id,
                                                                         @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated", maintenanceService.updateStatus(id, body.get("status"))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        maintenanceService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Request deleted"));
    }
}

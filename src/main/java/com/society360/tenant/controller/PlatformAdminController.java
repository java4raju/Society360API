package com.society360.tenant.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.common.dto.PageResponse;
import com.society360.tenant.dto.TenantResponse;
import com.society360.tenant.service.PlatformTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/platform")
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Platform — Super Admin")
public class PlatformAdminController {

    private final PlatformTenantService platformTenantService;

    @GetMapping("/tenants")
    @Operation(summary = "List all registered societies")
    public ResponseEntity<ApiResponse<PageResponse<TenantResponse>>> listTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(platformTenantService.listTenants(page, size)));
    }

    @PatchMapping("/tenants/{id}/status")
    @Operation(summary = "Activate, suspend or cancel a tenant")
    public ResponseEntity<ApiResponse<TenantResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok(
            platformTenantService.updateStatus(id, body.get("status"))));
    }

    @PostMapping("/tenants/{id}/extend-trial")
    @Operation(summary = "Extend trial period by N days")
    public ResponseEntity<ApiResponse<TenantResponse>> extendTrial(
            @PathVariable UUID id,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(ApiResponse.ok(
            platformTenantService.extendTrial(id, body.getOrDefault("days", 7))));
    }
}

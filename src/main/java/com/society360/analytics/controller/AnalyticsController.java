package com.society360.analytics.controller;

import com.society360.analytics.service.AnalyticsService;
import com.society360.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    @Operation(summary = "Dashboard KPI cards — residents, dues, complaints, finance summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> overview() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.overview()));
    }

    @GetMapping("/finance-trend")
    @Operation(summary = "Monthly income vs expense for last 12 months")
    public ResponseEntity<ApiResponse<Object>> financeTrend() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.financeTrend()));
    }

    @GetMapping("/complaints-by-category")
    @Operation(summary = "Complaint count grouped by category")
    public ResponseEntity<ApiResponse<Object>> complaintsByCategory() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.complaintsByCategory()));
    }

    @GetMapping("/occupancy")
    @Operation(summary = "Resident occupancy breakdown — Owner / Tenant / Vacant")
    public ResponseEntity<ApiResponse<Object>> occupancy() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.occupancy()));
    }
}

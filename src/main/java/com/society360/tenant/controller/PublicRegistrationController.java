package com.society360.tenant.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.tenant.dto.RegistrationRequest;
import com.society360.tenant.dto.RegistrationResponse;
import com.society360.tenant.service.TenantRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@Tag(name = "Public — Registration")
public class PublicRegistrationController {

    private final TenantRegistrationService registrationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new society (self-service, no auth required)")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(
            @Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok(registrationService.register(request)));
    }
}

package com.society360.vendor.controller;

import com.society360.common.dto.ApiResponse;
import com.society360.common.dto.PageResponse;
import com.society360.vendor.dto.VendorRequest;
import com.society360.vendor.dto.VendorResponse;
import com.society360.vendor.service.VendorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendors")
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VendorResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(vendorService.search(q, status, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(vendorService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VendorResponse>> create(@Valid @RequestBody VendorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Vendor added", vendorService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VendorResponse>> update(@PathVariable UUID id,
                                                              @Valid @RequestBody VendorRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Vendor updated", vendorService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        vendorService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Vendor deleted"));
    }
}

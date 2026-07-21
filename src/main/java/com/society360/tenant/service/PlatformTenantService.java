package com.society360.tenant.service;

import com.society360.common.dto.PageResponse;
import com.society360.tenant.dto.TenantResponse;
import com.society360.tenant.entity.Tenant;
import com.society360.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlatformTenantService {

    private final TenantRepository tenantRepository;

    public PageResponse<TenantResponse> listTenants(int page, int size) {
        return PageResponse.of(
            tenantRepository.findAll(PageRequest.of(page, size))
                .map(TenantResponse::from)
        );
    }

    @Transactional
    public TenantResponse updateStatus(UUID tenantId, String status) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new com.society360.common.exception.ResourceNotFoundException("Tenant", tenantId));
        tenant.setStatus(Tenant.TenantStatus.valueOf(status.toUpperCase()));
        tenant.setUpdatedAt(java.time.Instant.now());
        return TenantResponse.from(tenantRepository.save(tenant));
    }

    @Transactional
    public TenantResponse extendTrial(UUID tenantId, int days) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new com.society360.common.exception.ResourceNotFoundException("Tenant", tenantId));
        tenant.setTrialEndsAt(tenant.getTrialEndsAt().plus(days, java.time.temporal.ChronoUnit.DAYS));
        tenant.setUpdatedAt(java.time.Instant.now());
        return TenantResponse.from(tenantRepository.save(tenant));
    }
}

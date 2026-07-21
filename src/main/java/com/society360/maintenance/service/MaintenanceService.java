package com.society360.maintenance.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.maintenance.dto.MaintenanceRequestDto;
import com.society360.maintenance.dto.MaintenanceResponse;
import com.society360.maintenance.entity.MaintenanceRequest;
import com.society360.maintenance.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public PageResponse<MaintenanceResponse> search(String q, String status, String priority, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return PageResponse.of(maintenanceRepository.search(q, status, priority, pageable).map(MaintenanceResponse::from));
    }

    public MaintenanceResponse getById(UUID id) {
        return MaintenanceResponse.from(findOrThrow(id));
    }

    @Transactional
    public MaintenanceResponse create(MaintenanceRequestDto req) {
        MaintenanceRequest entity = MaintenanceRequest.builder()
            .title(req.title()).description(req.description()).category(req.category())
            .priority(req.priority()).status(req.status())
            .resident(req.resident()).flatNumber(req.flatNumber())
            .assignedTo(req.assignedTo()).scheduledDate(req.scheduledDate())
            .resolvedDate(req.resolvedDate())
            .build();
        return MaintenanceResponse.from(maintenanceRepository.save(entity));
    }

    @Transactional
    public MaintenanceResponse update(UUID id, MaintenanceRequestDto req) {
        MaintenanceRequest entity = findOrThrow(id);
        entity.setTitle(req.title()); entity.setDescription(req.description());
        entity.setCategory(req.category()); entity.setPriority(req.priority());
        entity.setStatus(req.status()); entity.setResident(req.resident());
        entity.setFlatNumber(req.flatNumber()); entity.setAssignedTo(req.assignedTo());
        entity.setScheduledDate(req.scheduledDate()); entity.setResolvedDate(req.resolvedDate());
        return MaintenanceResponse.from(maintenanceRepository.save(entity));
    }

    @Transactional
    public MaintenanceResponse assign(UUID id, String assignedTo) {
        MaintenanceRequest entity = findOrThrow(id);
        entity.setAssignedTo(assignedTo);
        entity.setStatus("Assigned");
        return MaintenanceResponse.from(maintenanceRepository.save(entity));
    }

    @Transactional
    public MaintenanceResponse updateStatus(UUID id, String status) {
        MaintenanceRequest entity = findOrThrow(id);
        entity.setStatus(status);
        return MaintenanceResponse.from(maintenanceRepository.save(entity));
    }

    @Transactional
    public void delete(UUID id) { maintenanceRepository.delete(findOrThrow(id)); }

    private MaintenanceRequest findOrThrow(UUID id) {
        return maintenanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MaintenanceRequest", id));
    }
}

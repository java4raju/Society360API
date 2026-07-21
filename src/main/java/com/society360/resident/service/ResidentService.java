package com.society360.resident.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.BusinessException;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.resident.dto.ResidentRequest;
import com.society360.resident.dto.ResidentResponse;
import com.society360.resident.entity.Resident;
import com.society360.resident.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResidentService {

    private final ResidentRepository residentRepository;

    public PageResponse<ResidentResponse> search(String q, Resident.Status status,
                                                  Resident.Occupancy occupancy, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("block", "flatNumber"));
        return PageResponse.of(residentRepository.search(q, status, occupancy, pageable)
            .map(ResidentResponse::from));
    }

    public ResidentResponse getById(UUID id) {
        return ResidentResponse.from(findOrThrow(id));
    }

    @Transactional
    public ResidentResponse create(ResidentRequest req) {
        if (residentRepository.existsByBlockAndFlatNumber(req.block(), req.flatNumber())) {
            throw new BusinessException("Flat " + req.block() + "-" + req.flatNumber() + " already exists");
        }
        Resident resident = Resident.builder()
            .block(req.block())
            .flatNumber(req.flatNumber())
            .ownerName(req.ownerName())
            .tenantName(req.tenantName())
            .contact(req.contact())
            .email(req.email())
            .parkingSlots(req.parkingSlots())
            .occupancy(req.occupancy())
            .status(req.status())
            .duesAmount(req.duesAmount() != null ? req.duesAmount() : BigDecimal.ZERO)
            .joinedDate(req.joinedDate() != null ? req.joinedDate() : LocalDate.now())
            .build();
        return ResidentResponse.from(residentRepository.save(resident));
    }

    @Transactional
    public ResidentResponse update(UUID id, ResidentRequest req) {
        Resident resident = findOrThrow(id);
        // If flat number changed, check uniqueness
        if (!resident.getBlock().equals(req.block()) || !resident.getFlatNumber().equals(req.flatNumber())) {
            if (residentRepository.existsByBlockAndFlatNumber(req.block(), req.flatNumber())) {
                throw new BusinessException("Flat " + req.block() + "-" + req.flatNumber() + " already exists");
            }
        }
        resident.setBlock(req.block());
        resident.setFlatNumber(req.flatNumber());
        resident.setOwnerName(req.ownerName());
        resident.setTenantName(req.tenantName());
        resident.setContact(req.contact());
        resident.setEmail(req.email());
        resident.setParkingSlots(req.parkingSlots());
        resident.setOccupancy(req.occupancy());
        resident.setStatus(req.status());
        if (req.duesAmount() != null) resident.setDuesAmount(req.duesAmount());
        if (req.joinedDate() != null) resident.setJoinedDate(req.joinedDate());
        return ResidentResponse.from(residentRepository.save(resident));
    }

    @Transactional
    public void delete(UUID id) {
        residentRepository.delete(findOrThrow(id));
    }

    @Transactional
    public int bulkImport(List<ResidentRequest> requests) {
        int count = 0;
        for (ResidentRequest req : requests) {
            if (!residentRepository.existsByBlockAndFlatNumber(req.block(), req.flatNumber())) {
                create(req);
                count++;
            }
        }
        return count;
    }

    private Resident findOrThrow(UUID id) {
        return residentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resident", id));
    }
}

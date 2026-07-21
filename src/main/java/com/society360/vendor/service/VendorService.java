package com.society360.vendor.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.vendor.dto.VendorRequest;
import com.society360.vendor.dto.VendorResponse;
import com.society360.vendor.entity.Vendor;
import com.society360.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VendorService {

    private final VendorRepository vendorRepository;

    public PageResponse<VendorResponse> search(String q, String status, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("name"));
        return PageResponse.of(vendorRepository.search(q, status, pageable).map(VendorResponse::from));
    }

    public VendorResponse getById(UUID id) {
        return VendorResponse.from(findOrThrow(id));
    }

    @Transactional
    public VendorResponse create(VendorRequest req) {
        Vendor vendor = Vendor.builder()
            .name(req.name()).category(req.category()).status(req.status())
            .contact(req.contact()).email(req.email())
            .contractValue(req.contractValue()).rating(req.rating())
            .contractStart(req.contractStart()).contractEnd(req.contractEnd())
            .build();
        return VendorResponse.from(vendorRepository.save(vendor));
    }

    @Transactional
    public VendorResponse update(UUID id, VendorRequest req) {
        Vendor vendor = findOrThrow(id);
        vendor.setName(req.name()); vendor.setCategory(req.category()); vendor.setStatus(req.status());
        vendor.setContact(req.contact()); vendor.setEmail(req.email());
        vendor.setContractValue(req.contractValue()); vendor.setRating(req.rating());
        vendor.setContractStart(req.contractStart()); vendor.setContractEnd(req.contractEnd());
        return VendorResponse.from(vendorRepository.save(vendor));
    }

    @Transactional
    public void delete(UUID id) { vendorRepository.delete(findOrThrow(id)); }

    private Vendor findOrThrow(UUID id) {
        return vendorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vendor", id));
    }
}

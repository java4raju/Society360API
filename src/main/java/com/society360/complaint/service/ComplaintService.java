package com.society360.complaint.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.complaint.dto.ComplaintRequest;
import com.society360.complaint.dto.ComplaintResponse;
import com.society360.complaint.entity.Complaint;
import com.society360.complaint.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public PageResponse<ComplaintResponse> search(String q, String status, String priority, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return PageResponse.of(complaintRepository.search(q, status, priority, pageable)
            .map(ComplaintResponse::from));
    }

    public ComplaintResponse getById(UUID id) {
        return ComplaintResponse.from(findOrThrow(id));
    }

    @Transactional
    public ComplaintResponse create(ComplaintRequest req) {
        Complaint complaint = Complaint.builder()
            .title(req.title())
            .description(req.description())
            .category(req.category())
            .priority(req.priority())
            .status(req.status())
            .resident(req.resident())
            .flatNumber(req.flatNumber())
            .assignedTo(req.assignedTo())
            .createdDate(req.createdDate())
            .resolvedDate(req.resolvedDate())
            .build();
        return ComplaintResponse.from(complaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponse update(UUID id, ComplaintRequest req) {
        Complaint complaint = findOrThrow(id);
        complaint.setTitle(req.title());
        complaint.setDescription(req.description());
        complaint.setCategory(req.category());
        complaint.setPriority(req.priority());
        complaint.setStatus(req.status());
        complaint.setResident(req.resident());
        complaint.setFlatNumber(req.flatNumber());
        complaint.setAssignedTo(req.assignedTo());
        complaint.setCreatedDate(req.createdDate());
        complaint.setResolvedDate(req.resolvedDate());
        return ComplaintResponse.from(complaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponse updateStatus(UUID id, String status) {
        Complaint complaint = findOrThrow(id);
        complaint.setStatus(status);
        return ComplaintResponse.from(complaintRepository.save(complaint));
    }

    @Transactional
    public void delete(UUID id) {
        complaintRepository.delete(findOrThrow(id));
    }

    private Complaint findOrThrow(UUID id) {
        return complaintRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Complaint", id));
    }
}

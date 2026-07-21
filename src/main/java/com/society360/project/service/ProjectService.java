package com.society360.project.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.project.dto.ProjectRequest;
import com.society360.project.dto.ProjectResponse;
import com.society360.project.entity.Project;
import com.society360.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    public PageResponse<ProjectResponse> search(String q, String status, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        return PageResponse.of(projectRepository.search(q, status, pageable).map(ProjectResponse::from));
    }

    public ProjectResponse getById(UUID id) {
        return ProjectResponse.from(findOrThrow(id));
    }

    @Transactional
    public ProjectResponse create(ProjectRequest req) {
        Project project = Project.builder()
            .name(req.name()).description(req.description()).category(req.category())
            .status(req.status()).budget(req.budget()).spent(req.spent())
            .progress(req.progress()).owner(req.owner())
            .startDate(req.startDate()).endDate(req.endDate())
            .build();
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse update(UUID id, ProjectRequest req) {
        Project project = findOrThrow(id);
        project.setName(req.name()); project.setDescription(req.description());
        project.setCategory(req.category()); project.setStatus(req.status());
        project.setBudget(req.budget()); project.setSpent(req.spent());
        project.setProgress(req.progress()); project.setOwner(req.owner());
        project.setStartDate(req.startDate()); project.setEndDate(req.endDate());
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse updateProgress(UUID id, int progress) {
        Project project = findOrThrow(id);
        project.setProgress(progress);
        if (progress == 100) project.setStatus("Completed");
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public void delete(UUID id) { projectRepository.delete(findOrThrow(id)); }

    private Project findOrThrow(UUID id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }
}

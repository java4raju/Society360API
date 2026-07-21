package com.society360.document.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.document.dto.DocumentResponse;
import com.society360.document.entity.Document;
import com.society360.document.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Value("${app.storage.upload-dir}")
    private String uploadDir;

    public PageResponse<DocumentResponse> search(String q, String category, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return PageResponse.of(documentRepository.search(q, category, pageable).map(DocumentResponse::from));
    }

    @Transactional
    public DocumentResponse upload(MultipartFile file, String name, String category) throws IOException {
        Path dir = Paths.get(uploadDir, "documents");
        Files.createDirectories(dir);
        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dest = dir.resolve(storedName);
        file.transferTo(dest);

        String uploader = SecurityContextHolder.getContext().getAuthentication().getName();
        Document doc = Document.builder()
            .name(name != null && !name.isBlank() ? name : file.getOriginalFilename())
            .fileName(file.getOriginalFilename())
            .filePath(dest.toString())
            .mimeType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
            .sizeBytes(file.getSize())
            .category(category != null ? category : "General")
            .uploadedBy(uploader)
            .build();
        return DocumentResponse.from(documentRepository.save(doc));
    }

    public Resource download(UUID id) throws MalformedURLException {
        Document doc = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document", id));
        Path path = Paths.get(doc.getFilePath());
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) throw new ResourceNotFoundException("Document file", id);
        return resource;
    }

    public DocumentResponse getById(UUID id) {
        return DocumentResponse.from(documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document", id)));
    }

    @Transactional
    public void delete(UUID id) {
        Document doc = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document", id));
        try { Files.deleteIfExists(Paths.get(doc.getFilePath())); } catch (IOException ignored) {}
        documentRepository.delete(doc);
    }
}

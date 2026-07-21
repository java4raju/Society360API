package com.society360.notice.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.notice.dto.NoticeRequest;
import com.society360.notice.dto.NoticeResponse;
import com.society360.notice.entity.Notice;
import com.society360.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public PageResponse<NoticeResponse> search(String q, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.of(noticeRepository.search(q, category, pageable).map(NoticeResponse::from));
    }

    public NoticeResponse getById(UUID id) {
        return NoticeResponse.from(findOrThrow(id));
    }

    @Transactional
    public NoticeResponse create(NoticeRequest req) {
        Notice notice = Notice.builder()
            .title(req.title()).body(req.body()).category(req.category())
            .author(req.author()).date(req.date()).pinned(req.pinned()).important(req.important())
            .build();
        return NoticeResponse.from(noticeRepository.save(notice));
    }

    @Transactional
    public NoticeResponse update(UUID id, NoticeRequest req) {
        Notice notice = findOrThrow(id);
        notice.setTitle(req.title()); notice.setBody(req.body()); notice.setCategory(req.category());
        notice.setAuthor(req.author()); notice.setDate(req.date());
        notice.setPinned(req.pinned()); notice.setImportant(req.important());
        return NoticeResponse.from(noticeRepository.save(notice));
    }

    @Transactional
    public void delete(UUID id) { noticeRepository.delete(findOrThrow(id)); }

    private Notice findOrThrow(UUID id) {
        return noticeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notice", id));
    }
}

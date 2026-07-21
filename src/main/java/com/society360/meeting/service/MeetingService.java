package com.society360.meeting.service;

import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.meeting.dto.MeetingRequest;
import com.society360.meeting.dto.MeetingResponse;
import com.society360.meeting.entity.Meeting;
import com.society360.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;

    @Value("${app.storage.upload-dir}")
    private String uploadDir;

    public PageResponse<MeetingResponse> search(String q, String status, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        return PageResponse.of(meetingRepository.search(q, status, pageable).map(MeetingResponse::from));
    }

    public MeetingResponse getById(UUID id) {
        return MeetingResponse.from(findOrThrow(id));
    }

    @Transactional
    public MeetingResponse create(MeetingRequest req) {
        Meeting meeting = Meeting.builder()
            .title(req.title())
            .type(req.type())
            .status(req.status())
            .date(req.date())
            .location(req.location())
            .attendees(req.attendees())
            .agenda(req.agenda() != null ? req.agenda() : new ArrayList<>())
            .decisions(req.decisions() != null ? req.decisions() : new ArrayList<>())
            .build();
        return MeetingResponse.from(meetingRepository.save(meeting));
    }

    @Transactional
    public MeetingResponse update(UUID id, MeetingRequest req) {
        Meeting meeting = findOrThrow(id);
        meeting.setTitle(req.title());
        meeting.setType(req.type());
        meeting.setStatus(req.status());
        meeting.setDate(req.date());
        meeting.setLocation(req.location());
        meeting.setAttendees(req.attendees());
        meeting.setAgenda(req.agenda() != null ? req.agenda() : new ArrayList<>());
        meeting.setDecisions(req.decisions() != null ? req.decisions() : new ArrayList<>());
        return MeetingResponse.from(meetingRepository.save(meeting));
    }

    @Transactional
    public MeetingResponse uploadMinutes(UUID id, MultipartFile file) throws IOException {
        Meeting meeting = findOrThrow(id);
        Path dir = Paths.get(uploadDir, "minutes");
        Files.createDirectories(dir);
        String filename = id + "_" + file.getOriginalFilename();
        Path dest = dir.resolve(filename);
        file.transferTo(dest);
        meeting.setMinutesFilePath("/uploads/minutes/" + filename);
        return MeetingResponse.from(meetingRepository.save(meeting));
    }

    @Transactional
    public void delete(UUID id) {
        meetingRepository.delete(findOrThrow(id));
    }

    private Meeting findOrThrow(UUID id) {
        return meetingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Meeting", id));
    }
}

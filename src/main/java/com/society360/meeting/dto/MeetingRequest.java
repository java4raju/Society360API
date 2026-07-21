package com.society360.meeting.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record MeetingRequest(
    @NotBlank String title,
    @NotBlank String type,
    @NotBlank String status,
    @NotNull LocalDate date,
    @NotBlank String location,
    @Min(0) int attendees,
    List<String> agenda,
    List<String> decisions
) {}

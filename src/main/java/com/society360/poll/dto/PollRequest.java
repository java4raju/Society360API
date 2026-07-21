package com.society360.poll.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record PollRequest(
    @NotBlank String title,
    String description,
    @NotBlank String category,
    @NotBlank String status,
    @NotNull LocalDate endDate,
    @NotEmpty @Size(min = 2) List<String> optionLabels
) {}

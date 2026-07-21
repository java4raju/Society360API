package com.society360.notice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NoticeRequest(
    @NotBlank String title,
    @NotBlank String body,
    @NotBlank String category,
    @NotBlank String author,
    @NotNull LocalDate date,
    boolean pinned,
    boolean important
) {}

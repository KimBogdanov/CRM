package ru.crm.system.dto.filter;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LessonFilter(LocalDate from,
                           LocalDate to,
                           MonthSchedule month) {
}
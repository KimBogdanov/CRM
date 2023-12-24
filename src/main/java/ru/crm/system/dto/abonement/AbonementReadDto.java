package ru.crm.system.dto.abonement;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record AbonementReadDto(Integer id,
                               Integer numberOfLessons,
                               BigDecimal balance,
                               String type,
                               LocalDate begin,
                               LocalDate expire,
                               String status) {
}
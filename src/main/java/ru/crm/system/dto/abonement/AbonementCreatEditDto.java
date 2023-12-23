package ru.crm.system.dto.abonement;

import lombok.Builder;
import ru.crm.system.database.entity.enums.AbonementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record AbonementCreatEditDto(Integer numberOfLessons,
                                    BigDecimal balance,
                                    String type,
                                    LocalDate begin,
                                    LocalDate expire,
                                    AbonementStatus status,
                                    Integer studentId) {
}
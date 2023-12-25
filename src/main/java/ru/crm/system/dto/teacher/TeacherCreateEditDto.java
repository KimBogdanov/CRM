package ru.crm.system.dto.teacher;

import lombok.Builder;
import ru.crm.system.database.entity.enums.TeacherStatus;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record TeacherCreateEditDto(String firstName,
                                   String lastName,
                                   String phone,
                                   String email,
                                   String rawPassword,
                                   String avatar,
                                   List<String> subjects,
                                   TeacherStatus status,
                                   BigDecimal salaryPerHour,
                                   Double payRatio) {
}
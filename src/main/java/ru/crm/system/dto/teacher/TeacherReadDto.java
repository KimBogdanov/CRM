package ru.crm.system.dto.teacher;

import lombok.Builder;
import ru.crm.system.database.entity.enums.TeacherStatus;

@Builder
public record TeacherReadDto(Integer id,
                             String firstName,
                             String lastName,
                             String phone,
                             String email,
                             String avatar,
                             TeacherStatus status,
                             Double payRatio) {
}
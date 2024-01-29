package ru.crm.system.dto.lesson;

import lombok.Builder;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonPayType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record LessonReadDto(Integer id,
                            String studentName,
                            String teacherName,
                            LocalDateTime lessonDateTime,
                            Integer duration,
                            String subject,
                            LessonStatus status,
                            LessonPayType type,
                            String description,
                            BigDecimal cost) {
}
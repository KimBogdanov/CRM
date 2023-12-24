package ru.crm.system.dto.lesson;

import lombok.Builder;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record LessonCreateEditDto(Integer studentId,
                                  Integer teacherId,
                                  LocalDateTime lessonDateTime,
                                  Integer duration,
                                  Integer subjectId,
                                  LessonStatus status,
                                  LessonType type,
                                  String description,
                                  BigDecimal cost) {
}
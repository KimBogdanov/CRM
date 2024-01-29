package ru.crm.system.dto.lesson;

import lombok.Builder;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record LessonCreateEditDto(Integer studentId,
                                  Integer teacherId,
                                  LocalDate date,
                                  LocalTime time,
                                  Integer duration,
                                  Integer subjectId,
                                  LessonStatus status,
                                  LessonPayType payType,
                                  LessonType type,
                                  String description,
                                  BigDecimal cost) {
}
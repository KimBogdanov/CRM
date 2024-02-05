package ru.crm.system.dto.lesson;

import lombok.Builder;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record LessonReadDto(Integer id,
                            List<String> studentName,
                            String teacherFullName,
                            LocalDate lessonDate,
                            LocalTime lessonTime,
                            Integer duration,
                            String subject,
                            LessonStatus status,
                            LessonType type,
                            LessonPayType payType,
                            String description,
                            BigDecimal cost) {
}
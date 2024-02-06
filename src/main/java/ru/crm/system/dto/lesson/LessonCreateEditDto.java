package ru.crm.system.dto.lesson;

import lombok.Builder;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.validation.CheckSameDataAndTime;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@CheckSameDataAndTime
public record LessonCreateEditDto(

        @NotEmpty(message = "{lesson.student_empty_list}")
        List<String> studentFullNames,

        @NotBlank(message = "{lesson.teacher_empty}")
        String teacherFullName,

        @NotNull(message = "{lesson.date_empty}")
        @FutureOrPresent(message = "{lesson.data_future_or_present}")
        LocalDate date,

        @NotNull(message = "{lesson.time_empty}")
        LocalTime time,

        @NotNull(message = "{lesson.duration_empty}")
        Integer duration,

        @NotNull(message = "{lesson.subject_empty}")
        Subject subject,

        LessonStatus status,

        @NotNull(message = "{lesson.pay_type_empty}")
        LessonPayType payType,

        @NotNull(message = "{lesson.type_empty}")
        LessonType type,

        String description,

        @NotNull(message = "{lesson.cost_empty}")
        BigDecimal cost) {
}
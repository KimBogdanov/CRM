package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.LessonService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class LessonServiceIT extends IntegrationTestBase {

    private final LessonService lessonService;

    @Test
    void create_shouldCreateNewLesson_whenCreateDtoValid() {
        var lessonCreateDto = getValidLessonCreateEditDto();

        var actualLessonReadDto = lessonService.create(lessonCreateDto);

        actualLessonReadDto.ifPresent(lesson ->
                assertAll(() -> {
                    assertThat(lesson.id()).isPositive();
                    assertThat(lesson.studentFullNames()).contains("Андрей Иванов", "Павел Петров");
                    assertThat(lesson.teacherFullName()).isEqualTo("Наталья Петрова");
                    assertThat(lesson.lessonDate()).isEqualTo("2024-02-15");
                    assertThat(lesson.lessonTime()).isEqualTo("10:00");
                    assertThat(lesson.duration()).isEqualTo(45);
                    assertThat(lesson.subject()).isEqualTo("Вокал");
                    assertThat(lesson.type()).isEqualTo(LessonType.GROUP);
                    assertThat(lesson.payType()).isEqualTo(LessonPayType.PAID);
                    assertThat(lesson.status()).isEqualTo(LessonStatus.APPOINTED);
                    assertThat(lesson.description()).isEqualTo("Первый урок по вокалу");
                    assertThat(lesson.cost()).isEqualTo(BigDecimal.valueOf(800));
                }));
    }

    private LessonCreateEditDto getValidLessonCreateEditDto() {
        return LessonCreateEditDto.builder()
                .studentFullNames(List.of("Андрей Иванов", "Павел Петров"))
                .teacherFullName("Наталья Петрова")
                .date(LocalDate.of(2024, 2, 15))
                .time(LocalTime.of(10, 0))
                .duration(45)
                .subject(Subject.builder().name("Вокал").build())
                .type(LessonType.GROUP)
                .payType(LessonPayType.PAID)
                .description("Первый урок по вокалу")
                .cost(BigDecimal.valueOf(800))
                .build();
    }
}
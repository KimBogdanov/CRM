package ru.crm.system.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.database.repository.LessonRepository;
import ru.crm.system.integration.IntegrationTestBase;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class LessonRepositoryIT extends IntegrationTestBase {

    private final LessonRepository lessonRepository;

    @Test
    void findByDataAndTime_shouldFindLessonByDataAndTime() {
        var actualLesson = lessonRepository.findByDataAndTime(LocalDate.of(2023, 12, 10),
                LocalTime.of(10, 0));

        assertThat(actualLesson).isPresent();
        actualLesson.ifPresent(lesson ->
                assertAll(() -> {
                    assertThat(lesson.getStudents()).hasSize(3);
                    assertThat(lesson.getTeacher().getId()).isEqualTo(1);
                    assertThat(lesson.getDate().toString()).isEqualTo("2023-12-10");
                    assertThat(lesson.getTime().toString()).isEqualTo("10:00");
                    assertThat(lesson.getDuration()).isEqualTo(45);
                    assertThat(lesson.getSubject().getId()).isEqualTo(1);
                    assertThat(lesson.getStatus()).isEqualTo(LessonStatus.APPOINTED);
                    assertThat(lesson.getPayType()).isEqualTo(LessonPayType.PAID);
                    assertThat(lesson.getLessonType()).isEqualTo(LessonType.GROUP);
                    assertThat(lesson.getCost().toString()).isEqualTo("450.00");
                }));
    }
}
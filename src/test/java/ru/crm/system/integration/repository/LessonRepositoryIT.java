package ru.crm.system.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.database.entity.enums.TeacherStatus;
import ru.crm.system.database.repository.LessonRepository;
import ru.crm.system.dto.filter.LessonFilter;
import ru.crm.system.integration.IntegrationTestBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class LessonRepositoryIT extends IntegrationTestBase {

    private static final Integer EXISTING_LESSON_ID = 1;

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

    @Test
    void findById_shouldNotHaveSavedAtAndModifiedAtFields() {
        var existingLesson = lessonRepository.findById(EXISTING_LESSON_ID);

        existingLesson.ifPresent(lesson -> {
            assertThat(lesson.getSavedAt()).isNull();
            assertThat(lesson.getModifiedAt()).isNull();
        });
    }

    @Test
    void save_shouldSetSavedAtOnly_whenSavingNewLesson() {
        var lessonToSave = getLesson();
        var savedLesson = lessonRepository.save(lessonToSave);

        assertThat(savedLesson.getSavedAt()).isNotNull();
        assertThat(savedLesson.getModifiedAt()).isNull();
    }

    @Test
    void update_shouldSetModifiedAt_whenUpdatingExistingLesson() {
        var lessonToSave = getLesson();
        var savedLesson = lessonRepository.save(lessonToSave);

        assertThat(savedLesson.getModifiedAt()).isNull();

        savedLesson.setDescription("Description to check modifiedAt");
        lessonRepository.flush();
        var modifiedLesson = lessonRepository.findById(savedLesson.getId());

        modifiedLesson.ifPresent(lesson -> assertThat(lesson.getModifiedAt()).isNotNull());
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForFindAllByFilter")
    void findAllByFilter_shouldFindLessonsByFilter(LessonFilter filter, int expectedNumberOfLesson) {
        var actualLessons = lessonRepository.findAllByFilter(filter);

        assertThat(actualLessons).hasSize(expectedNumberOfLesson);
    }

    static Stream<Arguments> getArgumentsForFindAllByFilter() {
        return Stream.of(
                Arguments.of(LessonFilter.builder().from(LocalDate.of(2023, 12, 10))
                        .to(LocalDate.of(2023, 12, 10))
                        .build(), 9),
                Arguments.of(LessonFilter.builder()
                        .from(LocalDate.of(2023, 12, 10))
                        .to(LocalDate.of(2024, 2, 14))
                        .build(), 14)
        );
    }

    private Lesson getLesson() {
        return Lesson.builder()
                .students(List.of(getStudent()))
                .teacher(getTeacher())
                .date(LocalDate.of(2024, 2, 15))
                .time(LocalTime.of(10, 0))
                .duration(45)
                .subject(Subject.builder().name("Хоровое пение").build())
                .lessonType(LessonType.GROUP)
                .payType(LessonPayType.PAID)
                .status(LessonStatus.APPOINTED)
                .description("Первый урок по вокалу")
                .cost(BigDecimal.valueOf(800))
                .build();
    }

    private Student getStudent() {
        var studentInfo = UserInfo.builder()
                .firstName("Андрей")
                .lastName("Иванов")
                .phone("77-777-777-77")
                .role(Role.STUDENT)
                .build();

        return Student.builder()
                .userInfo(studentInfo)
                .build();
    }

    private Teacher getTeacher() {
        var teacherInfo = UserInfo.builder()
                .firstName("Наталья")
                .lastName("Петрова")
                .phone("888-888-888-888")
                .email("natalya_teacher@gmail.com")
                .password("test_password")
                .role(Role.TEACHER)
                .build();

        return Teacher.builder()
                .userInfo(teacherInfo)
                .status(TeacherStatus.ACTIVE)
                .build();
    }
}
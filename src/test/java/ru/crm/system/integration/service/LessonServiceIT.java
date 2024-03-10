package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.database.repository.LogInfoRepository;
import ru.crm.system.database.repository.StudentRepository;
import ru.crm.system.database.repository.TeacherRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.LessonService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
public class LessonServiceIT extends IntegrationTestBase {

    private static final Integer EXISTING_LESSON_ID = 1;
    public static final Integer EXISTING_TEACHER_ID = 1;

    private final LessonService lessonService;
    private final LogInfoRepository logInfoRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Test
    void create_shouldCreateNewLesson_whenCreateDtoValid() {
        var lessonCreateDto = getValidLessonCreateEditDto();

        var actualLessonReadDto = lessonService.create(lessonCreateDto);

        actualLessonReadDto.ifPresent(lesson ->
                assertAll(() -> {
                    assertThat(lesson.id()).isPositive();
                    assertThat(lesson.studentFullNames()).contains("Андрей Иванов", "Павел Петров");
                    assertThat(lesson.teacherFullName()).isEqualTo("Наталья Петрова");
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

    @Test
    void create_shouldThrowValidationExceptions() {
        var lessonCreateDto = getInvalidLessonCreateEditDto();
        var exception = assertThrows(ConstraintViolationException.class, () -> lessonService.create(lessonCreateDto));

        assertThat(exception.getConstraintViolations()).hasSize(9);
    }

    @Test
    void create_shouldThrowValidationException_ifThereIsLessonInPastAndWithSameDataAndTime() {
        var lessonCreateDto = getLessonCreateEditDtoInPastWithTheSameDataAndTime();
        var exception = assertThrows(ConstraintViolationException.class, () -> lessonService.create(lessonCreateDto));
        var messages = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();

        assertThat(messages).hasSize(2);
        assertThat(messages).contains("На выбранную дату и время уже назначен урок.", "Дата проведения урока указана не верно.");
    }

    @Test
    void create_shouldCreateLessonLogInfo_whenCreatingNewLesson() {
        var logInfos = logInfoRepository.findAll();
        assertThat(logInfos).isEmpty();

        var lessonCreateDto = getValidLessonCreateEditDto();
        var createdLesson = lessonService.create(lessonCreateDto);

        createdLesson.ifPresent(lesson -> {
            var logInfosByLesson = logInfoRepository.findAllByLessonId(lesson.id());
            assertThat(logInfosByLesson).hasSize(1);
            var lessonIdAtLogInfo = logInfosByLesson.stream().map(logInfo -> logInfo.getLesson().getId()).findFirst();
            lessonIdAtLogInfo.ifPresent(id -> assertThat(id).isEqualTo(lesson.id()));
        });
    }

    @Test
    void update_shouldUpdateExistingLesson_ifEditDtoValid() {
        var existingLesson = lessonService.findById(EXISTING_LESSON_ID);

        existingLesson.ifPresent(lesson ->
                assertAll(() -> {
                    assertThat(lesson.id()).isEqualTo(EXISTING_LESSON_ID);
                    assertThat(lesson.studentFullNames()).isEqualTo(List.of("Андрей Иванов", "Павел Петров", "Катя Степанова"));
                    assertThat(lesson.teacherFullName()).isEqualTo("Наталья Петрова");
                    assertThat(lesson.subject()).isEqualTo("Вокал");
                    assertThat(lesson.status()).isEqualTo(LessonStatus.APPOINTED);
                }));

        var validLessonEditDto = getValidLessonCreateEditDto();

        var actualLesson = lessonService.update(EXISTING_LESSON_ID, validLessonEditDto);
        actualLesson.ifPresent(lesson ->
                assertAll(() -> {
                    assertThat(lesson.id()).isEqualTo(EXISTING_LESSON_ID);
                    assertThat(lesson.studentFullNames()).isEqualTo(List.of("Андрей Иванов", "Павел Петров"));
                    assertThat(lesson.teacherFullName()).isEqualTo("Наталья Петрова");
                    assertThat(lesson.subject()).isEqualTo("Вокал");
                    assertThat(lesson.status()).isEqualTo(LessonStatus.APPOINTED);
                })
        );
    }

    @Test
    void update_shouldThrowValidationException_ifEditDtoInvalid() {
        var invalidLessonEditDto = getInvalidLessonCreateEditDto();

        var exception = assertThrows(ConstraintViolationException.class,
                () -> lessonService.update(EXISTING_LESSON_ID, invalidLessonEditDto));

        assertThat(exception.getConstraintViolations()).hasSize(9);
    }

    @Test
    void changeLessonStatus_shouldChangeLessonStatus_ifLessonExists() {
        var existingLesson = lessonService.findById(EXISTING_LESSON_ID);
        existingLesson.ifPresent(lesson -> assertThat(lesson.status()).isEqualTo(LessonStatus.APPOINTED));

        var actualLesson = lessonService.changeLessonStatus(EXISTING_LESSON_ID, LessonStatus.SUCCESSFULLY_FINISHED);

        actualLesson.ifPresent(lesson -> assertThat(lesson.status()).isEqualTo(LessonStatus.SUCCESSFULLY_FINISHED));
    }

    @Test
    void changeLessonStatus_shouldCreateLogs_whenLessonFinishedSuccessfully() {
        var allLogsBeforeChangeLessonStatus = logInfoRepository.findAll();
        assertThat(allLogsBeforeChangeLessonStatus).hasSize(0);

        lessonService.changeLessonStatus(EXISTING_LESSON_ID, LessonStatus.SUCCESSFULLY_FINISHED);

        var allLogsAfterChangLessonStatus = logInfoRepository.findAll();
        var logsByLessonAfterChangeStatus = logInfoRepository.findAllByLessonId(EXISTING_LESSON_ID);
        var logsByTeacherAfterChangeStatus = logInfoRepository.findAllByTeacherId(EXISTING_TEACHER_ID);
        var studentLogInfos = logInfoRepository.findAllByStudentIds(1, 2, 3);

        assertAll(() -> {
            assertThat(allLogsAfterChangLessonStatus).hasSize(4);
            assertThat(logsByLessonAfterChangeStatus).hasSize(4);
            assertThat(logsByTeacherAfterChangeStatus).hasSize(4);
            assertThat(studentLogInfos).hasSize(3);
        });
    }

    @Test
    void changeLessonStatus_shouldWriteOffMoneyFromStudentAccounts_whenLessonFinishedSuccessfully() {
        var existingLesson = lessonService.findById(EXISTING_LESSON_ID);
        var studentBalances = existingLesson.stream()
                .map(LessonReadDto::studentFullNames)
                .map(studentRepository::findAllByFullNames)
                .flatMap(Collection::stream)
                .map(Student::getAbonement)
                .map(Abonement::getBalance)
                .map(BigDecimal::toString)
                .toList();

        assertThat(studentBalances).contains("4000.00", "8000.00", "4000.00");

        var existingLessonAfterChangingStatus = lessonService.changeLessonStatus(EXISTING_LESSON_ID, LessonStatus.SUCCESSFULLY_FINISHED);

        var studentBalancesAfterLesson = existingLessonAfterChangingStatus.stream()
                .map(LessonReadDto::studentFullNames)
                .map(studentRepository::findAllByFullNames)
                .flatMap(Collection::stream)
                .map(Student::getAbonement)
                .map(Abonement::getBalance)
                .map(BigDecimal::toString)
                .toList();

        assertThat(studentBalancesAfterLesson).contains("3550.00", "7550.00", "3550.00");
    }

    @Test
    void changeLessonStatus_shouldSubtractOneLessonFromStudentsAccount_whenLessonFinishedSuccessfully() {
        var numberOfLessonsOnAbonementByStudentBeforeLesosn = lessonService.findById(EXISTING_LESSON_ID).stream()
                .map(LessonReadDto::studentFullNames)
                .map(studentRepository::findAllByFullNames)
                .flatMap(Collection::stream)
                .map(Student::getAbonement)
                .map(Abonement::getNumberOfLessons)
                .toList();

        assertThat(numberOfLessonsOnAbonementByStudentBeforeLesosn).containsExactly(4, 8, 4);

        var numberOfLessonsAfterSuccessfulLesson = lessonService.changeLessonStatus(EXISTING_LESSON_ID,
                        LessonStatus.SUCCESSFULLY_FINISHED).stream()
                .map(LessonReadDto::studentFullNames)
                .map(studentRepository::findAllByFullNames)
                .flatMap(Collection::stream)
                .map(Student::getAbonement)
                .map(Abonement::getNumberOfLessons)
                .toList();

        assertThat(numberOfLessonsAfterSuccessfulLesson).containsExactly(3, 7, 3);
    }

    @Test
    void changeLessonStatus_shouldCreateTeachersAndStudentLogs_whenLessonCanceledForGoodReason() {
        var allLogsBeforeChangingStatus = logInfoRepository.findAll();

        assertThat(allLogsBeforeChangingStatus).hasSize(0);

        lessonService.changeLessonStatus(EXISTING_LESSON_ID, LessonStatus.CANCELED_FOR_GOOD_REASON);

        var allLogsAfterChangingStatus = logInfoRepository.findAll();
        var teacherLogsAfterChangingStatus = logInfoRepository.findAllByTeacherId(EXISTING_TEACHER_ID);
        var studentLogsAfterChangingStatus = logInfoRepository.findAllByStudentIds(1, 2, 3);

        assertAll(() -> {
            assertThat(allLogsAfterChangingStatus).hasSize(4);
            assertThat(teacherLogsAfterChangingStatus).hasSize(4);
            assertThat(studentLogsAfterChangingStatus).hasSize(3);
        });
    }

    @Test
    void changeLessonStatus_shouldCreateLogs_whenLessonCanceledByTeacherFault() {
        var allLogsBeforeChangingStatus = logInfoRepository.findAll();

        assertThat(allLogsBeforeChangingStatus).hasSize(0);

        lessonService.changeLessonStatus(EXISTING_LESSON_ID, LessonStatus.CANCELED_BY_TEACHER_FAULT);

        var allLogsAfterChangingStatus = logInfoRepository.findAll();
        var teacherLogsAfterChangingStatus = logInfoRepository.findAllByTeacherId(EXISTING_TEACHER_ID);
        var studentLogsAfterChangingStatus = logInfoRepository.findAllByStudentIds(1, 2, 3);

        assertAll(() -> {
            assertThat(allLogsAfterChangingStatus).hasSize(1);
            assertThat(teacherLogsAfterChangingStatus).hasSize(1);
            assertThat(studentLogsAfterChangingStatus).hasSize(0);
        });
    }

    private LessonCreateEditDto getValidLessonCreateEditDto() {
        return LessonCreateEditDto.builder()
                .studentFullNames(List.of("Андрей Иванов", "Павел Петров"))
                .teacherFullName("Наталья Петрова")
                .date(LocalDate.now())
                .time(LocalTime.of(10, 0))
                .duration(45)
                .subject(Subject.builder().name("Вокал").build())
                .status(LessonStatus.APPOINTED)
                .type(LessonType.GROUP)
                .payType(LessonPayType.PAID)
                .description("Первый урок по вокалу")
                .cost(BigDecimal.valueOf(800))
                .build();
    }

    private LessonCreateEditDto getInvalidLessonCreateEditDto() {
        return LessonCreateEditDto.builder()
                .build();
    }

    private LessonCreateEditDto getLessonCreateEditDtoInPastWithTheSameDataAndTime() {
        return LessonCreateEditDto.builder()
                .studentFullNames(List.of("Андрей Иванов", "Павел Петров"))
                .teacherFullName("Наталья Петрова")
                .date(LocalDate.of(2023, 12, 10))
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
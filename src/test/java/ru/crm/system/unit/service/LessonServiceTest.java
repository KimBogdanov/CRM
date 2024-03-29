package ru.crm.system.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.database.repository.LessonRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.mapper.lesson.LessonCreateEditMapper;
import ru.crm.system.mapper.lesson.LessonReadMapper;
import ru.crm.system.service.LessonService;
import ru.crm.system.service.LogInfoService;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class LessonServiceTest {

    private static final Integer EXISTING_LESSON_ID = 1;

    @Captor
    private ArgumentCaptor<Lesson> lessonCaptor;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private LessonCreateEditMapper lessonCreateEditMapper;
    @Mock
    private LessonReadMapper lessonReadMapper;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private LogInfoService logInfoService;
    @Mock
    private Validator validator;
    @InjectMocks
    private LessonService lessonService;

    @Test
    void create_shouldCreateNewLesson_whenLessonCreateDtoValid() {
        var validLessonCreateDto = getValidLessonCreateEditDto();
        var lesson = getLesson();
        var lessonReadDto = getLessonReadDto();

        when(validator.validate(validLessonCreateDto)).thenReturn(Collections.emptySet());
        when(lessonCreateEditMapper.map(validLessonCreateDto)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonReadMapper.map(lesson)).thenReturn(lessonReadDto);

        var actualLessonReadDto = lessonService.create(validLessonCreateDto);

        actualLessonReadDto.ifPresent(lessonDto ->
                assertAll(() -> {
                    assertThat(lessonDto.id()).isPositive();
                    assertThat(lessonDto.studentFullNames()).contains("Андрей Иванов", "Павел Петров");
                    assertThat(lessonDto.teacherFullName()).isEqualTo("Наталья Петрова");
                    assertThat(lessonDto.lessonDate()).isEqualTo("2024-02-15");
                    assertThat(lessonDto.lessonTime()).isEqualTo("10:00");
                    assertThat(lessonDto.duration()).isEqualTo(45);
                    assertThat(lessonDto.subject()).isEqualTo("Вокал");
                    assertThat(lessonDto.type()).isEqualTo(LessonType.GROUP);
                    assertThat(lessonDto.payType()).isEqualTo(LessonPayType.PAID);
                    assertThat(lessonDto.description()).isEqualTo("Первый урок по вокалу");
                    assertThat(lessonDto.cost()).isEqualTo(BigDecimal.valueOf(800));
                }));
        verify(lessonRepository).save(lessonCaptor.capture());
        var capturedLesson = lessonCaptor.getValue();
        assertThat(capturedLesson).isEqualTo(lesson);
        verify(logInfoService).createLogInfoWhenLessonAppointed(lesson);
        verify(publisher).publishEvent(any());
    }

    @Test
    void create_shouldReturnEmpty_whenLessonCreateDtoInvalid() {
        var invalidLessonCreateDto = getInvalidLessonCreateEditDto();

        var actualLessonReadDto = lessonService.create(invalidLessonCreateDto);

        assertThat(actualLessonReadDto).isEmpty();

        verify(validator).validate(invalidLessonCreateDto);
        verifyNoInteractions(lessonRepository, publisher, logInfoService);
    }

    @Test
    void create_shouldThrowValidationException_whenLessonCreateDtoInvalid() {
        var invalidLessonCreateDto = getInvalidLessonCreateEditDto();
        when(validator.validate(invalidLessonCreateDto)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> lessonService.create(invalidLessonCreateDto));
        assertThatThrownBy(() -> lessonService.create(invalidLessonCreateDto)).isInstanceOf(ConstraintViolationException.class);

        verify(validator, times(2)).validate(any());
        verifyNoInteractions(lessonCreateEditMapper, lessonReadMapper, publisher, logInfoService);
    }

    @Test
    void update_shouldUpdateExistingLesson_whenLessonUpdateIsValid() {
        var validLessonEditDto = getValidLessonCreateEditDto();
        var existingLesson = getLesson();
        var lessonReadDto = getLessonReadDto();

        when(lessonRepository.findById(EXISTING_LESSON_ID)).thenReturn(Optional.of(existingLesson));
        when(lessonCreateEditMapper.map(validLessonEditDto, existingLesson)).thenReturn(existingLesson);
        when(lessonRepository.saveAndFlush(existingLesson)).thenReturn(existingLesson);
        when(lessonReadMapper.map(existingLesson)).thenReturn(lessonReadDto);

        var actualLesson = lessonService.update(EXISTING_LESSON_ID, validLessonEditDto);

        assertThat(actualLesson).isPresent();
        actualLesson.ifPresent(lesson ->
                assertAll(() -> {
                    assertThat(lesson.id()).isEqualTo(EXISTING_LESSON_ID);
                    assertThat(lesson.subject()).isEqualTo("Вокал");
                    assertThat(lesson.teacherFullName()).isEqualTo("Наталья Петрова");
                }));

        verify(validator).validate(validLessonEditDto);
    }

    @Test
    void update_shouldThrowValidationExceptions_whenLessonUpdateIsInvalid() {
        var invalidLessonEditDto = getInvalidLessonCreateEditDto();
        when(validator.validate(invalidLessonEditDto)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> lessonService.update(EXISTING_LESSON_ID, invalidLessonEditDto));

        verify(validator).validate(any());
        verifyNoInteractions(lessonRepository, lessonCreateEditMapper, lessonReadMapper);
    }

    @Test
    void update_shouldReturnEmpty_whenLessonUpdateIsInvalid() {
        var invalidLessonEditDto = getInvalidLessonCreateEditDto();
        var actualLesson = lessonService.update(EXISTING_LESSON_ID, invalidLessonEditDto);

        assertThat(actualLesson).isEmpty();

        verify(validator).validate(any());
        verify(lessonRepository).findById(EXISTING_LESSON_ID);
        verifyNoInteractions(lessonCreateEditMapper, lessonReadMapper);
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

    private Lesson getLesson() {
        var firstStudent = getStudent();
        firstStudent.setUserInfo(UserInfo.builder().firstName("Андрей").lastName("Иванов").build());
        firstStudent.setId(1);
        var secondStudent = getStudent();
        secondStudent.setUserInfo(UserInfo.builder().firstName("Павел").lastName("Петров").build());

        return Lesson.builder()
                .id(EXISTING_LESSON_ID)
                .students(List.of(firstStudent, secondStudent))
                .teacher(getTeacher())
                .date(LocalDate.of(2024, 2, 15))
                .time(LocalTime.of(10, 0))
                .duration(45)
                .subject(Subject.builder().name("Вокал").build())
                .lessonType(LessonType.GROUP)
                .payType(LessonPayType.PAID)
                .description("Первый урок по вокалу")
                .cost(BigDecimal.valueOf(800))
                .build();
    }

    private LessonCreateEditDto getInvalidLessonCreateEditDto() {
        return LessonCreateEditDto.builder()
                .build();
    }

    private Student getStudent() {
        return Student.builder().build();
    }

    private Teacher getTeacher() {
        var teacherInfo = UserInfo.builder()
                .firstName("Наталья")
                .lastName("Петрова")
                .build();
        return Teacher.builder()
                .userInfo(teacherInfo)
                .build();
    }

    private LessonReadDto getLessonReadDto() {
        return LessonReadDto.builder()
                .id(EXISTING_LESSON_ID)
                .studentFullNames(List.of("Андрей Иванов", "Павел Петров"))
                .teacherFullName("Наталья Петрова")
                .lessonDate(LocalDate.of(2024, 2, 15))
                .lessonTime(LocalTime.of(10, 0))
                .duration(45)
                .subject("Вокал")
                .type(LessonType.GROUP)
                .payType(LessonPayType.PAID)
                .description("Первый урок по вокалу")
                .cost(BigDecimal.valueOf(800))
                .build();
    }
}
package ru.crm.system.unit.mapper.lesson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.database.repository.StudentRepository;
import ru.crm.system.database.repository.SubjectRepository;
import ru.crm.system.database.repository.TeacherRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.mapper.lesson.LessonCreateEditMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonCreateEditMapperTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private LessonCreateEditMapper lessonCreateEditMapper;

    @Test
    void map_shouldMapFromLessonCreateDto_ToLessonEntity() {
        var expectedLesson = getLesson();
        var createDto = getLessonCreateEditDto();
        var teacher = getTeacher();
        var subject = getSubject();
        when(teacherRepository.findByFullName("Наталья Петрова")).thenReturn(Optional.of(teacher));
        doReturn(Optional.of(subject)).when(subjectRepository).getSubjectByName("Вокал");

        var actualLesson = lessonCreateEditMapper.map(createDto);

        assertThat(actualLesson).isEqualTo(expectedLesson);
    }

    @Test
    void map_shouldMapFromLessonCreateDtoToLessonEntity_whenUpdateExistingLesson() {
        var existingLesson = getLesson();
        var editDto = getLessonCreateEditDto();
        var teacher = getTeacher();
        var subject = getSubject();
        when(teacherRepository.findByFullName("Наталья Петрова")).thenReturn(Optional.of(teacher));
        doReturn(Optional.of(subject)).when(subjectRepository).getSubjectByName("Вокал");

        var actualLesson = lessonCreateEditMapper.map(editDto, existingLesson);

        assertThat(actualLesson).isEqualTo(existingLesson);
    }

    private LessonCreateEditDto getLessonCreateEditDto() {
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
        var secondStudent = getStudent();
        secondStudent.setUserInfo(UserInfo.builder().firstName("Павел").lastName("Петров").build());

        return Lesson.builder()
                .students(List.of(firstStudent, secondStudent))
                .teacher(getTeacher())
                .date(LocalDate.of(2024, 2, 15))
                .time(LocalTime.of(10, 0))
                .duration(45)
                .subject(Subject.builder().name("Вокал").build())
                .lessonType(LessonType.GROUP)
                .status(LessonStatus.APPOINTED)
                .payType(LessonPayType.PAID)
                .description("Первый урок по вокалу")
                .cost(BigDecimal.valueOf(800))
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

    private Subject getSubject() {
        return Subject.builder()
                .name("Вокал")
                .build();
    }
}
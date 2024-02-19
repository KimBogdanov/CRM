package ru.crm.system.unit.mapper.lesson;

import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.mapper.lesson.LessonReadMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LessonReadMapperTest {

    private final LessonReadMapper lessonReadMapper = new LessonReadMapper();

    @Test
    void map_shouldMapFromLessonEntity_toLessonReadDto() {
        var actualLessonReadDto = getLessonReadDto();
        var lesson = getLesson();
        var expectedLessonReadDto = lessonReadMapper.map(lesson);

        assertThat(actualLessonReadDto).isEqualTo(expectedLessonReadDto);
    }

    private LessonReadDto getLessonReadDto() {
        return LessonReadDto.builder()
                .id(1)
                .studentFullNames(List.of("Андрей Иванов", "Павел Петров"))
                .teacherFullName("Наталья Петрова")
                .lessonDate(LocalDate.of(2024, 2, 15))
                .lessonTime(LocalTime.of(10, 0))
                .duration(45)
                .subject("Вокал")
                .status(LessonStatus.APPOINTED)
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
                .id(1)
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
}
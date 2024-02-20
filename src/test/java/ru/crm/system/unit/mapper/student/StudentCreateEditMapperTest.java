package ru.crm.system.unit.mapper.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.AbonementStatus;
import ru.crm.system.database.entity.enums.AbonementType;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.database.repository.SubjectRepository;
import ru.crm.system.dto.student.StudentCreateEditDto;
import ru.crm.system.mapper.student.StudentCreateEditMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCreateEditMapperTest {

    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private StudentCreateEditMapper studentCreateEditMapper;

    @Test
    void map_shouldMapFromLessonCreatDto_toLessonEntity() {
        var expectedStudent = getStudent();
        var studentCreateDto = getStudentCreateDto();
        when(subjectRepository.getSubjectByName("Вокал")).thenReturn(Optional.of(getSubject()));

        var actualStudent = studentCreateEditMapper.map(studentCreateDto);

        assertAll(() -> {
            assertThat(actualStudent.getUserInfo().getFirstName()).isEqualTo(expectedStudent.getUserInfo().getFirstName());
            assertThat(actualStudent.getUserInfo().getLastName()).isEqualTo(expectedStudent.getUserInfo().getLastName());
            assertThat(actualStudent.getUserInfo().getPhone()).isEqualTo(expectedStudent.getUserInfo().getPhone());
            assertThat(actualStudent.getSubject().getName()).isEqualTo(expectedStudent.getSubject().getName());
        });

        assertThat(actualStudent).isEqualTo(expectedStudent);
    }

    private Student getStudent() {
        var studentInfo = UserInfo.builder()
                .firstName("Андрей")
                .lastName("Иванов")
                .phone("8-699-755-75-85")
                .email("andrey@gmail.com")
                .role(Role.STUDENT)
                .build();
        return Student.builder()
                .userInfo(studentInfo)
                .subject(getSubject())
                .abonement(getAbonement())
                .build();
    }

    private StudentCreateEditDto getStudentCreateDto() {
        return StudentCreateEditDto.builder()
                .firstName("Андрей")
                .lastName("Иванов")
                .phone("8-699-755-75-85")
                .email("andrey@gmail.com")
                .subject(getSubject().getName())
                .build();
    }

    private Subject getSubject() {
        return Subject.builder()
                .name("Вокал")
                .build();
    }

    private Abonement getAbonement() {
        return Abonement.builder()
                .id(1)
                .numberOfLessons(8)
                .balance(BigDecimal.valueOf(4800))
                .type(AbonementType.GROUP)
                .begin(LocalDate.of(2024, 3, 3))
                .expire(LocalDate.of(2024, 5, 3))
                .status(AbonementStatus.ACTIVE)
                .build();
    }
}
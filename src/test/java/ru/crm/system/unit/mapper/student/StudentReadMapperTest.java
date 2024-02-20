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
import ru.crm.system.database.repository.AbonementRepository;
import ru.crm.system.dto.student.StudentReadDto;
import ru.crm.system.mapper.student.StudentReadMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentReadMapperTest {

    @Mock
    private AbonementRepository abonementRepository;
    @InjectMocks
    private StudentReadMapper studentReadMapper;

    @Test
    void map_shouldMapFromStudentEntity_toStudentReadDto() {
        var studentEntity = getStudent();
        var expectedStudentReadDto = getStudentReadDto();
        when(abonementRepository.getBalanceByStudent(studentEntity.getId())).thenReturn(BigDecimal.valueOf(2500));

        var actualStudentReadDto = studentReadMapper.map(studentEntity);

        assertAll(() -> {
            assertThat(actualStudentReadDto.id()).isEqualTo(expectedStudentReadDto.id());
            assertThat(actualStudentReadDto.firstName()).isEqualTo(expectedStudentReadDto.firstName());
            assertThat(actualStudentReadDto.lastName()).isEqualTo(expectedStudentReadDto.lastName());
            assertThat(actualStudentReadDto.phone()).isEqualTo(expectedStudentReadDto.phone());
            assertThat(actualStudentReadDto.subject()).isEqualTo(expectedStudentReadDto.subject());
            assertThat(actualStudentReadDto.balance()).isEqualTo(BigDecimal.valueOf(2500));
        });
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
                .id(1)
                .userInfo(studentInfo)
                .subject(getSubject())
                .abonement(getAbonement())
                .build();
    }

    private StudentReadDto getStudentReadDto() {
        return StudentReadDto.builder()
                .id(1)
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
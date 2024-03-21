package ru.crm.system.unit.mapper.abonement;

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
import ru.crm.system.database.repository.StudentRepository;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.mapper.abonement.AbonementCreateEditMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AbonementCreateEditMapperTest {

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private AbonementCreateEditMapper abonementCreateEditMapper;

    @Test
    void map_shouldMapCreateDtoToEntity() {
        var abonementCreatEditDto = getAbonementCreatEditDto();
        var expectedAbonement = getAbonement();
        var actualAbonement = abonementCreateEditMapper.map(abonementCreatEditDto);

        assertThat(actualAbonement.toString()).isEqualTo(expectedAbonement.toString());
    }

    private AbonementCreatEditDto getAbonementCreatEditDto() {
        return AbonementCreatEditDto.builder()
                .numberOfLessons(8)
                .balance(BigDecimal.valueOf(4800))
                .type(AbonementType.GROUP)
                .begin(LocalDate.of(2024, 3, 3))
                .expire(LocalDate.of(2024, 5, 3))
                .status(AbonementStatus.ACTIVE)
                .build();
    }

    private Abonement getAbonement() {
        return Abonement.builder()
                .numberOfLessons(8)
                .balance(BigDecimal.valueOf(4800))
                .type(AbonementType.GROUP)
                .begin(LocalDate.of(2024, 3, 3))
                .expire(LocalDate.of(2024, 5, 3))
                .status(AbonementStatus.ACTIVE)
                .student(getStudent())
                .build();
    }

    private Student getStudent() {
        var studentInfo = UserInfo.builder()
                .firstName("Виктор")
                .lastName("Петров")
                .phone("8-954-954-55-55")
                .email("victor@gmail.com")
                .role(Role.STUDENT)
                .avatar("avatar.jpg")
                .build();
        return Student.builder()
                .userInfo(studentInfo)
                .subject(Subject.builder().name("Гитара").build())
                .build();
    }
}
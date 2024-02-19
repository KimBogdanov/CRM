package ru.crm.system.unit.mapper.abonement;

import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.AbonementStatus;
import ru.crm.system.database.entity.enums.AbonementType;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.dto.abonement.AbonementReadDto;
import ru.crm.system.mapper.abonement.AbonementReadMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AbonementReadMapperTest {

    private final AbonementReadMapper abonementReadMapper = new AbonementReadMapper();

    @Test
    void map_shouldMapFromAbonementEntityToAbonementReadDto() {
        var abonement = getAbonement();
        var expectedReadDto = getAbonementReadDto();
        var actualReadDto = abonementReadMapper.map(abonement);

        assertThat(actualReadDto).isEqualTo(expectedReadDto);
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

    private AbonementReadDto getAbonementReadDto() {
        return AbonementReadDto.builder()
                .id(1)
                .numberOfLessons(8)
                .balance(BigDecimal.valueOf(4800))
                .type(AbonementType.GROUP.name())
                .begin(LocalDate.of(2024, 3, 3))
                .expire(LocalDate.of(2024, 5, 3))
                .status(AbonementStatus.ACTIVE.name())
                .build();
    }
}
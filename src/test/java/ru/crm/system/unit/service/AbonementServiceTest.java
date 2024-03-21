package ru.crm.system.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.AbonementStatus;
import ru.crm.system.database.entity.enums.AbonementType;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.database.repository.AbonementRepository;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.dto.abonement.AbonementReadDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.abonement.AbonementCreateEditMapper;
import ru.crm.system.mapper.abonement.AbonementReadMapper;
import ru.crm.system.service.AbonementService;
import ru.crm.system.service.LogInfoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbonementServiceTest {

    private static final Integer EXISTING_ABONEMENT_ID = 1;
    private static final Integer EXISTING_STUDENT_ID = 1;
    private static final Integer EXISTING_ADMIN_ID = 1;
    private static final Integer EXISTING_ORDER_ID = 1;

    @Mock
    private AbonementRepository abonementRepository;
    @Mock
    private AbonementReadMapper abonementReadMapper;
    @Mock
    private AbonementCreateEditMapper abonementCreateEditMapper;
    @Mock
    private LogInfoService logInfoService;
    @Mock
    private ApplicationEventPublisher publisher;
    @InjectMocks
    private AbonementService abonementService;

    @Test
    void findById_shouldFindAbonementById_ifAbonementExists() {
        var expectedAbonement = getAbonement();
        var abonementReadDto = getAbonementReadDto();
        when(abonementRepository.findById(EXISTING_ABONEMENT_ID)).thenReturn(Optional.of(expectedAbonement));
        when(abonementReadMapper.map(expectedAbonement)).thenReturn(abonementReadDto);

        var actualAbonement = abonementService.findById(EXISTING_ABONEMENT_ID);

        actualAbonement.ifPresent(abonement ->
                assertAll(() -> {
                    assertThat(abonement.id()).isEqualTo(expectedAbonement.getId());
                    assertThat(abonement.numberOfLessons()).isEqualTo(expectedAbonement.getNumberOfLessons());
                    assertThat(abonement.balance()).isEqualTo(expectedAbonement.getBalance());
                    assertThat(abonement.type()).isEqualTo(expectedAbonement.getType().name());
                    assertThat(abonement.status()).isEqualTo(expectedAbonement.getStatus().name());
                    assertThat(abonement.begin()).isEqualTo(expectedAbonement.getBegin());
                    assertThat(abonement.expire()).isEqualTo(expectedAbonement.getExpire());
                }));
    }

    @Test
    void create_shouldCreateNewAbonement_whenCreateDtoIsValid() {
        var validAbonementCreateDto = getAbonementCreateDto();
        var abonement = getAbonement();
        var expectedAbonementReadDto = getAbonementReadDto();
        when(abonementCreateEditMapper.map(validAbonementCreateDto)).thenReturn(abonement);
        when(abonementRepository.save(abonement)).thenReturn(abonement);
        when(abonementReadMapper.map(abonement)).thenReturn(expectedAbonementReadDto);

        var actualAbonementReadDto = abonementService.create(validAbonementCreateDto, EXISTING_ADMIN_ID, EXISTING_ORDER_ID);

        assertThat(actualAbonementReadDto).isEqualTo(expectedAbonementReadDto);
    }

    @Test
    void create_shouldCrateLogInfoAndPublishIt_whenAbonementSavedInDb() {
        var validAbonementCreateDto = getAbonementCreateDto();
        var abonement = getAbonement();
        var expectedAbonementReadDto = getAbonementReadDto();
        when(abonementCreateEditMapper.map(validAbonementCreateDto)).thenReturn(abonement);
        when(abonementRepository.save(abonement)).thenReturn(abonement);
        when(abonementReadMapper.map(abonement)).thenReturn(expectedAbonementReadDto);

        abonementService.create(validAbonementCreateDto, EXISTING_ADMIN_ID, EXISTING_ORDER_ID);

        verify(logInfoService).createAbonementLogInfo(validAbonementCreateDto, EXISTING_ADMIN_ID, EXISTING_ORDER_ID);
        verify(publisher).publishEvent(new EntityEvent<>(abonement, AccessType.CREATE, any()));
    }

    private Abonement getAbonement() {
        return Abonement.builder()
                .id(EXISTING_ABONEMENT_ID)
                .numberOfLessons(5)
                .balance(BigDecimal.valueOf(6500))
                .type(AbonementType.INDIVIDUAL)
                .begin(LocalDate.now())
                .expire(LocalDate.now().plus(4L, ChronoUnit.MONTHS))
                .status(AbonementStatus.ACTIVE)
                .student(getStudent())
                .build();
    }

    private Student getStudent() {
        var studentInfo = UserInfo.builder()
                .firstName("Анатолий")
                .lastName("Петров")
                .role(Role.STUDENT)
                .email("Petrov@gmail.com")
                .phone("8-924-889-89-89")
                .build();
        return Student.builder()
                .id(EXISTING_STUDENT_ID)
                .userInfo(studentInfo)
                .build();
    }

    private AbonementReadDto getAbonementReadDto() {
        return AbonementReadDto.builder()
                .id(EXISTING_ABONEMENT_ID)
                .numberOfLessons(5)
                .balance(BigDecimal.valueOf(6500))
                .type(AbonementType.INDIVIDUAL.name())
                .begin(LocalDate.now())
                .expire(LocalDate.now().plus(4L, ChronoUnit.MONTHS))
                .status(AbonementStatus.ACTIVE.name())
                .build();
    }

    private AbonementCreatEditDto getAbonementCreateDto() {
        return AbonementCreatEditDto.builder()
                .numberOfLessons(5)
                .balance(BigDecimal.valueOf(6500))
                .type(AbonementType.INDIVIDUAL)
                .begin(LocalDate.now())
                .expire(LocalDate.now().plus(4L, ChronoUnit.MONTHS))
                .status(AbonementStatus.ACTIVE)
                .studentId(EXISTING_STUDENT_ID)
                .build();
    }
}
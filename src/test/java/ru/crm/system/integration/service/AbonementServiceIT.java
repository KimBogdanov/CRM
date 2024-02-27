package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.enums.AbonementStatus;
import ru.crm.system.database.entity.enums.AbonementType;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.AbonementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class AbonementServiceIT extends IntegrationTestBase {

    private static final Integer EXISTING_ABONEMENT_ID = 1;
    private static final Integer EXISTING_STUDENT_ID = 1;
    private static final Integer EXISTING_ADMIN_ID = 1;
    private static final Integer EXISTING_ORDER_ID = 1;

    private final AbonementService abonementService;

    @Test
    void findById_shouldFindAbonementById_ifAbonementExists() {
        var actualAbonement = abonementService.findById(EXISTING_ABONEMENT_ID);

        actualAbonement.ifPresent(abonement ->
                assertAll(() -> {
                    assertThat(abonement.id()).isEqualTo(EXISTING_ABONEMENT_ID);
                    assertThat(abonement.numberOfLessons()).isEqualTo(4);
                    assertThat(abonement.type()).isEqualTo(AbonementType.INDIVIDUAL.name());
                    assertThat(abonement.status()).isEqualTo(AbonementStatus.ACTIVE.name());
                    assertThat(abonement.begin()).isEqualTo("2023-12-10");
                    assertThat(abonement.expire()).isEqualTo("2023-12-31");
                }));
    }

    @Test
    void create_shouldCreateNewAbonement_whenCreateDtoIsValid() {
        var validAbonementCreateDto = getAbonementCreateDto();

        var actualAbonementReadDto = abonementService.create(validAbonementCreateDto, EXISTING_ADMIN_ID, EXISTING_ORDER_ID);

        assertAll(() -> {
            assertThat(actualAbonementReadDto.id()).isPositive();
            assertThat(actualAbonementReadDto.numberOfLessons()).isEqualTo(validAbonementCreateDto.numberOfLessons());
            assertThat(actualAbonementReadDto.balance()).isEqualTo(validAbonementCreateDto.balance());
            assertThat(actualAbonementReadDto.status()).isEqualTo(validAbonementCreateDto.status().name());
            assertThat(actualAbonementReadDto.type()).isEqualTo(validAbonementCreateDto.type().name());
            assertThat(actualAbonementReadDto.begin()).isEqualTo(validAbonementCreateDto.begin());
            assertThat(actualAbonementReadDto.expire()).isEqualTo(validAbonementCreateDto.expire());
        });
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
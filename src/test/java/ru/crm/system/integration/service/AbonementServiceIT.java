package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.enums.AbonementStatus;
import ru.crm.system.database.entity.enums.AbonementType;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.AbonementService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class AbonementServiceIT extends IntegrationTestBase {

    private static final Integer EXISTING_ABONEMENT_ID = 1;

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
}
package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.dto.admin.AdminCreateEditDto;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.AdminService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class AdminServiceIT extends IntegrationTestBase {

    private static final Integer EXISTING_ADMIN_ID = 1;

    private final AdminService adminService;

    @Test
    void create_shouldSaveAdminToDb_whenCreateDtoValid() {
        var adminCreateDto = getAdminCreateDto();

        var actualAdmin = adminService.create(adminCreateDto);
        assertThat(actualAdmin.id()).isPositive();
    }

    @Test
    void findById_shouldFindById_ifAdminExists() {
        var actualAdmin = adminService.findById(EXISTING_ADMIN_ID);

        assertThat(actualAdmin).isPresent();

        actualAdmin.ifPresent(admin ->
                assertAll(() -> {
                    assertThat(admin.id()).isEqualTo(EXISTING_ADMIN_ID);
                    assertThat(admin.firstName()).isEqualTo("Андрей");
                    assertThat(admin.lastName()).isEqualTo("Админов");
                    assertThat(admin.phone()).isEqualTo("8-925-999-99-99");
                    assertThat(admin.email()).isEqualTo("adminFirst@gmail.com");
                }));
    }

    private AdminCreateEditDto getAdminCreateDto() {
        return AdminCreateEditDto.builder()
                .firstName("AdminFirstName")
                .lastName("AdminLastName")
                .phone("8-924-775-58-58")
                .email("admin@gmail.com")
                .rawPassword("rawPassword")
                .shiftRate(BigDecimal.valueOf(2500))
                .build();
    }
}
package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.crm.system.dto.AdminCreateEditDto;
import ru.crm.system.integration.IT;
import ru.crm.system.service.AdminService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class AdminServiceIT {

    private final AdminService adminService;

    @Test
    void create_shouldSaveAdminToDb_whenCreateDtoValid() {
        var adminCreateDto = getAdminCreateDto();

        var actualAdmin = adminService.create(adminCreateDto);
        assertThat(actualAdmin.id()).isPositive();
    }

    private AdminCreateEditDto getAdminCreateDto() {
        return AdminCreateEditDto.builder()
                .firstName("AdminFirstName")
                .lastName("AdminLastName")
                .phone("8-924-775-58-58")
                .email("admin@gmail.com")
                .rawPassword("rawPassword")
                .avatar(new MockMultipartFile(
                        "Admin.jpg",
                        "Admin.jpg",
                        MediaType.APPLICATION_OCTET_STREAM_VALUE,
                        "Admin.jpg".getBytes()))
                .shiftRate(BigDecimal.valueOf(2500))
                .build();
    }
}

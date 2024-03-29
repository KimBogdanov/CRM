package ru.crm.system.unit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.crm.system.database.entity.Admin;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.dto.admin.AdminCreateEditDto;
import ru.crm.system.dto.admin.AdminReadDto;
import ru.crm.system.mapper.AdminCreateEditMapper;
import ru.crm.system.mapper.AdminReadMapper;
import ru.crm.system.service.AdminService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    private static final Integer EXISTING_ADMIN_ID = 1;
    private static final Integer NOT_EXISTING_ADMIN_ID = 999;

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private AdminReadMapper adminReadMapper;
    @Mock
    private AdminCreateEditMapper adminCreateEditMapper;
    @InjectMocks
    private AdminService adminService;

    @Test
    void create_shouldSaveAdminToDb_whenCreateDtoValid() {
        var adminCreateDto = getAdminCreateDto();
        var admin = getAdmin();
        var adminReadDto = getAdminReadDto();

        when(adminCreateEditMapper.map(adminCreateDto)).thenReturn(admin);
        when(adminRepository.save(admin)).thenReturn(admin);
        when(adminReadMapper.map(admin)).thenReturn(adminReadDto);

        var actualAdmin = adminService.create(adminCreateDto);

        assertAll(() -> {
            assertThat(actualAdmin.firstName()).isEqualTo("AdminFirstName");
            assertThat(actualAdmin.lastName()).isEqualTo("AdminLastName");
            assertThat(actualAdmin.phone()).isEqualTo("8-924-775-58-58");
            assertThat(actualAdmin.avatar()).isEqualTo("Admin.jpg");
            assertThat(actualAdmin.shiftRate()).isEqualTo(BigDecimal.valueOf(3000));
        });
    }

    @Test
    void findById_shouldFindById_ifAdminExists() {
        var existingAdmin = getAdmin();
        var adminReadDto = getAdminReadDto();

        when(adminRepository.findById(EXISTING_ADMIN_ID)).thenReturn(Optional.of(existingAdmin));
        when(adminReadMapper.map(existingAdmin)).thenReturn(adminReadDto);
        var actualAdmin = adminService.findById(EXISTING_ADMIN_ID);

        assertThat(actualAdmin).isPresent();
        actualAdmin.ifPresent(admin -> assertThat(admin).isEqualTo(adminReadDto));
    }

    @Test
    void findById_shouldReturnEmpty_ifAdminNotExist() {
        when(adminRepository.findById(NOT_EXISTING_ADMIN_ID)).thenReturn(Optional.empty());

        var actualAdmin = adminService.findById(NOT_EXISTING_ADMIN_ID);

        assertThat(actualAdmin).isEmpty();
        verifyNoInteractions(adminReadMapper);
    }

    private AdminCreateEditDto getAdminCreateDto() {
        return AdminCreateEditDto.builder()
                .firstName("AdminFirstName")
                .lastName("AdminLastName")
                .phone("8-924-775-58-58")
                .email("admin@gmail.com")
                .rawPassword("rawPassword")
                .shiftRate(BigDecimal.valueOf(3000))
                .build();
    }

    private Admin getAdmin() {
        var adminInfo = UserInfo.builder()
                .firstName("AdminFirstName")
                .lastName("AdminLastName")
                .phone("8-924-775-58-58")
                .email("admin@gmail.com")
                .password("rawPassword")
                .role(Role.ADMIN)
                .avatar("Admin.jpg")
                .build();

        return Admin.builder()
                .userInfo(adminInfo)
                .shiftRate(BigDecimal.valueOf(3000))
                .build();
    }

    private AdminReadDto getAdminReadDto() {
        return AdminReadDto.builder()
                .firstName("AdminFirstName")
                .lastName("AdminLastName")
                .phone("8-924-775-58-58")
                .email("admin@gmail.com")
                .avatar("Admin.jpg")
                .shiftRate(BigDecimal.valueOf(3000))
                .build();
    }
}
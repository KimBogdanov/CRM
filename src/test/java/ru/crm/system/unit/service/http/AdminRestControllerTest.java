package ru.crm.system.unit.service.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.crm.system.dto.admin.AdminCreateEditDto;
import ru.crm.system.dto.admin.AdminReadDto;
import ru.crm.system.http.rest.AdminRestController;
import ru.crm.system.service.AdminService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(AdminRestController.class)
public class AdminRestControllerTest {

    private static final String BASE_ADMIN_URL = "/api/v1/admins/";
    private static final Integer EXISTING_ADMIN_ID = 1;
    private static final Integer NOT_EXISTING_ADMIN_ID = 999;

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    @MockBean
    private final AdminService adminService;

    @Test
    void create_shouldCreateAdmin_andReturn201() throws Exception {
        var adminCreateDto = getAdminCreateDto();
        var adminReadDto = getAdminReadDto();
        var requestBody = mapper.writeValueAsString(adminCreateDto);

        when(adminService.create(adminCreateDto)).thenReturn(adminReadDto);

        mockMvc.perform(post(BASE_ADMIN_URL + "create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.firstName", is("AdminFirstName")),
                        jsonPath("$.lastName", is("AdminLastName")),
                        jsonPath("$.phone", is("8-924-775-58-58")),
                        jsonPath("$.email", is("admin@gmail.com")));
        verify(adminService).create(adminCreateDto);
    }

    @Test
    void findById_shouldReturn200_whenAdminExists() throws Exception {
        var admin = getAdminReadDto();
        when(adminService.findById(EXISTING_ADMIN_ID)).thenReturn(Optional.of(admin));

        mockMvc.perform(get(BASE_ADMIN_URL + EXISTING_ADMIN_ID))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.firstName", is("AdminFirstName")),
                        jsonPath("$.lastName", is("AdminLastName")),
                        jsonPath("$.phone", is("8-924-775-58-58")),
                        jsonPath("$.email", is("admin@gmail.com")));
    }

    @Test
    void findById_shouldReturn404_whenAdminNotExists() throws Exception {
        mockMvc.perform(get(BASE_ADMIN_URL + NOT_EXISTING_ADMIN_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(adminService).findById(NOT_EXISTING_ADMIN_ID);
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
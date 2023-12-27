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

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(AdminRestController.class)
public class AdminRestControllerTest {

    private static final String BASE_ADMIN_URL = "/api/v1/admins";

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

        mockMvc.perform(post(BASE_ADMIN_URL + "/create")
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
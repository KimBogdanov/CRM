package ru.crm.system.integration.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.crm.system.dto.admin.AdminCreateEditDto;
import ru.crm.system.http.rest.AdminRestController;
import ru.crm.system.integration.IT;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@RequiredArgsConstructor
@AutoConfigureMockMvc
public class AdminRestControllerIT {

    private static final String BASE_ADMIN_URL = "/api/v1/admins";

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private final AdminRestController adminRestController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminRestController)
                .alwaysDo(print())
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    void create_shouldCreateAdmin_andReturn201() throws Exception {
        var adminCreateDto = getAdminCreateDto();
        var requestBody = mapper.writeValueAsString(adminCreateDto);
        mockMvc.perform(post(BASE_ADMIN_URL + "/create")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.firstName", is("AdminFirstName")));
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
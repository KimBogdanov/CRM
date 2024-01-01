package ru.crm.system.integration.http;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.http.rest.OrderRestController;
import ru.crm.system.integration.IT;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class OrderRestControllerIT {

    private static final String BASE_ORDER_URL = "/api/v1/orders/";
    private static final Integer EXISTING_ORDER_ID = 1;
    private static final Integer NOT_EXISTING_ORDER_ID = 999;

    private final OrderRestController orderRestController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
                .alwaysDo(print())
                .build();
    }

    @Test
    void findById_shouldReturn200_whenOrderExists() throws Exception {
        mockMvc.perform(get(BASE_ORDER_URL + EXISTING_ORDER_ID))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.status", is("UNPROCESSED"), OrderStatus.class),
                        jsonPath("$.orderName", is("Глинка/Вокал")),
                        jsonPath("$.clientName", is("Илья")),
                        jsonPath("$.phone", is("8-924-555-55-55")),
                        jsonPath("$.requestSource", is("Yandex"))
                );
    }

    @Test
    void findById_shouldReturn404_ifOrderNotExist() throws Exception {
        var mvcResult = mockMvc.perform(get(BASE_ORDER_URL + NOT_EXISTING_ORDER_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(requireNonNull(mvcResult.getResolvedException()).getClass()).hasSameClassAs(NotFoundException.class);
    }
}
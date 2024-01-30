package ru.crm.system.unit.http;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.http.rest.OrderRestController;
import ru.crm.system.service.OrderService;
import ru.crm.system.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
@RequiredArgsConstructor
public class OrderRestControllerTest {

    private static final String BASE_ORDER_URL = "/api/v1/orders/";
    private static final Integer EXISTING_ORDER_ID = 1;
    private static final Integer NOT_EXISTING_ORDER_ID = 999;

    @MockBean
    private final OrderService orderService;
    private final MockMvc mockMvc;

    @Test
    void findById_shouldReturn200_ifOrderExists() throws Exception {
        var orderReadDto = getOrderReadDto();
        when(orderService.findById(EXISTING_ORDER_ID)).thenReturn(Optional.of(orderReadDto));

        mockMvc.perform(get(BASE_ORDER_URL + EXISTING_ORDER_ID))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        var captor = ArgumentCaptor.forClass(Integer.class);
        verify(orderService).findById(captor.capture());
        assertThat(captor.getValue()).isEqualTo(EXISTING_ORDER_ID);
    }

    @Test
    void findById_shouldReturn404_ifOrderNotExist() throws Exception {
        var mvcResult = mockMvc.perform(get(BASE_ORDER_URL + NOT_EXISTING_ORDER_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
        var captor = ArgumentCaptor.forClass(Integer.class);

        assertThat(requireNonNull(mvcResult.getResolvedException()).getClass()).hasSameClassAs(NotFoundException.class);
        verify(orderService).findById(captor.capture());
        verify(orderService).findById(any());
        assertThat(captor.getValue()).isEqualTo(NOT_EXISTING_ORDER_ID);
    }

    @Test
    void findAll_shouldReturn200_whenOrdersExist() throws Exception {
        var orderReadDto = getOrderReadDto();
        when(orderService.findAll()).thenReturn(List.of(orderReadDto, orderReadDto));
        mockMvc.perform(get(BASE_ORDER_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private OrderReadDto getOrderReadDto() {
        return OrderReadDto.builder()
                .id(EXISTING_ORDER_ID)
                .status(OrderStatus.UNPROCESSED)
                .orderName("Глинка/Вокал")
                .clientName("Андрей")
                .phone("8-924-989-59-04")
                .requestSource("Yandex")
                .createdAt(DateTimeUtil.formatLocalDateTime(LocalDateTime.of(2023, 12, 15, 10, 15)))
                .build();
    }
}
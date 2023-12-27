package ru.crm.system.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import ru.crm.system.database.entity.Order;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.mapper.OrderCreateEditMapper;
import ru.crm.system.mapper.OrderReadMapper;
import ru.crm.system.service.OrderService;
import ru.crm.system.util.DateTimeUtil;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Integer EXISTING_ORDER_ID = 1;

    @Mock
    private OrderCreateEditMapper orderCreateEditMapper;
    @Mock
    private OrderReadMapper orderReadMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher publisher;
    @InjectMocks
    private OrderService orderService;

    @Test
    void create_shouldCreateOrderAndLogInfo() {
        var createDto = getOrderCreateEditDto();
        var orderReadDto = getOrderReadDto();
        var order = getOrder();

        when(orderCreateEditMapper.map(createDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderReadMapper.map(order)).thenReturn(orderReadDto);
        var actualOrder = orderService.create(createDto);

        assertAll(() -> {
            assertThat(actualOrder.id()).isEqualTo(EXISTING_ORDER_ID);
            assertThat(actualOrder.status()).isEqualTo(OrderStatus.UNPROCESSED);
            assertThat(actualOrder.orderName()).isEqualTo("Глинка/Вокал");
            assertThat(actualOrder.clientName()).isEqualTo("Андрей");
            assertThat(actualOrder.phone()).isEqualTo("8-924-989-59-04");
            assertThat(actualOrder.requestSource()).isEqualTo("Yandex");
            assertThat(actualOrder.createdAt()).isEqualTo("15-12-2023 10:15");
        });
        var orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        verify(publisher).publishEvent(any());
    }

    private OrderCreateEditDto getOrderCreateEditDto() {
        return OrderCreateEditDto.builder()
                .status(OrderStatus.UNPROCESSED)
                .orderName("Глинка/Вокал")
                .clientName("Андрей")
                .phone("8-924-989-59-04")
                .requestSource("Yandex")
                .createdAt(LocalDateTime.of(2023, 12, 15, 10, 15))
                .build();
    }

    private Order getOrder() {
        return Order.builder()
                .id(EXISTING_ORDER_ID)
                .status(OrderStatus.UNPROCESSED)
                .orderName("Глинка/Вокал")
                .clientName("Андрей")
                .phone("8-924-989-59-04")
                .requestSource("Yandex")
                .createdAt(LocalDateTime.of(2023, 12, 15, 10, 15))
                .build();
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
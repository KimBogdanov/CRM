package ru.crm.system.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import ru.crm.system.database.entity.Order;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.mapper.OrderCreateEditMapper;
import ru.crm.system.service.OrderService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Integer EXISTING_ORDER_ID = 1;
    private static final Integer EXISTING_ADMIN_ID = 1;

    @Mock
    private OrderCreateEditMapper orderCreateEditMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher publisher;
    @InjectMocks
    private OrderService orderService;

    @Test
    void create_shouldCreateOrderAndLogInfo() {
        var createDto = getOrderCreateEditDto();
        var order = getOrder();

        when(orderCreateEditMapper.map(createDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        var orderId = orderService.create(createDto);

        assertThat(orderId).isEqualTo(EXISTING_ORDER_ID);
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
}
package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.integration.IT;
import ru.crm.system.service.OrderService;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@IT
@RequiredArgsConstructor
public class OrderServiceIT {

    private static final Integer EXISTING_ORDER_ID = 1;
    private static final Integer NOT_EXISTING_ORDER_ID = 999;

    private final OrderService orderService;

    @Test
    void create_shouldCreateOrderAndLogInfo() {
        var createDto = getOrderCreateEditDto();

        var actualOrder = orderService.create(createDto);

        assertThat(actualOrder.id()).isPositive();
    }


    @Test
    void findById_shouldReturnOrder_whenOrderExists() {
        var actualOrder = orderService.findById(EXISTING_ORDER_ID);

        Assertions.assertThat(actualOrder).isPresent();
        actualOrder.ifPresent(order ->
                assertAll(() -> {
                    Assertions.assertThat(order.id()).isEqualTo(EXISTING_ORDER_ID);
                    Assertions.assertThat(order.status()).isEqualTo(OrderStatus.UNPROCESSED);
                    Assertions.assertThat(order.orderName()).isEqualTo("Глинка/Вокал");
                    Assertions.assertThat(order.clientName()).isEqualTo("Илья");
                    Assertions.assertThat(order.phone()).isEqualTo("8-924-555-55-55");
                    Assertions.assertThat(order.requestSource()).isEqualTo("Yandex");
                }));
    }

    @Test
    void findById_shouldReturnEmpty_whenOrderNotExist() {
        var actualOrder = orderService.findById(NOT_EXISTING_ORDER_ID);

        Assertions.assertThat(actualOrder).isEmpty();
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
}
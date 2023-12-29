package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.integration.IT;
import ru.crm.system.service.OrderService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@IT
@RequiredArgsConstructor
public class OrderServiceIT {

    private static final Integer EXISTING_ORDER_ID = 1;
    private static final Integer NOT_EXISTING_ORDER_ID = 999;
    private static final Integer EXISING_ADMIN_ID = 1;

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

        assertThat(actualOrder).isPresent();
        actualOrder.ifPresent(order ->
                assertAll(() -> {
                    assertThat(order.id()).isEqualTo(EXISTING_ORDER_ID);
                    assertThat(order.status()).isEqualTo(OrderStatus.UNPROCESSED);
                    assertThat(order.orderName()).isEqualTo("Глинка/Вокал");
                    assertThat(order.clientName()).isEqualTo("Илья");
                    assertThat(order.phone()).isEqualTo("8-924-555-55-55");
                    assertThat(order.requestSource()).isEqualTo("Yandex");
                }));
    }

    @Test
    void findById_shouldReturnEmpty_whenOrderNotExist() {
        var actualOrder = orderService.findById(NOT_EXISTING_ORDER_ID);

        assertThat(actualOrder).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllOrders_whenOrdersExist() {
        var actualOrders = orderService.findAll();
        var actualClientNames = actualOrders.stream()
                .map(OrderReadDto::clientName)
                .toList();

        assertThat(actualOrders).hasSize(5);
        assertThat(actualClientNames).containsExactly("Илья", "Андрей", "Пётр", "Маша", "Света");
    }

    @Test
    void update_shouldUpdateExistingOrderAndSetAdminId_whenOrderExistsAdnUnprocessed() {
        var existingOrder = orderService.findById(EXISTING_ORDER_ID);
        var updateDto = getOrderCreateEditDto();

        var actualOrder = orderService.update(EXISTING_ORDER_ID, EXISING_ADMIN_ID, updateDto);

        existingOrder.ifPresent(order -> {
            assertThat(order.adminId()).isNull();
            assertThat(order.status()).isEqualTo(OrderStatus.UNPROCESSED);
        });
        assertThat(actualOrder).isPresent();
        actualOrder.ifPresent(order ->
                assertAll(() -> {
                    assertThat(order.id()).isEqualTo(EXISTING_ORDER_ID);
                    assertThat(order.status()).isEqualTo(OrderStatus.UNPROCESSED);
                    assertThat(order.orderName()).isEqualTo("Глинка/Вокал");
                    assertThat(order.clientName()).isEqualTo("Андрей");
                    assertThat(order.phone()).isEqualTo("8-924-989-59-04");
                    assertThat(order.requestSource()).isEqualTo("Yandex");
                    assertThat(order.adminId()).isEqualTo(EXISING_ADMIN_ID);
                }));
    }

    private OrderCreateEditDto getOrderCreateEditDto() {
        return OrderCreateEditDto.builder()
                .adminId(EXISING_ADMIN_ID)
                .status(OrderStatus.UNPROCESSED)
                .orderName("Глинка/Вокал")
                .clientName("Андрей")
                .phone("8-924-989-59-04")
                .requestSource("Yandex")
                .createdAt(LocalDateTime.of(2023, 12, 15, 10, 15))
                .build();
    }
}
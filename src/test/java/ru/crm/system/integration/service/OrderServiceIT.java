package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.dto.OrderCreateEditDto;
import ru.crm.system.integration.IT;
import ru.crm.system.service.OrderService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class OrderServiceIT {

    private static final Integer EXISTING_ADMIN_ID = 1;

    private final OrderService orderService;

    @Test
    void create_shouldCreateOrderAndLogInfo() {
        var createDto = getOrderCreateEditDto();

        var orderId = orderService.create(createDto);

        assertThat(orderId).isPositive();
    }

    private OrderCreateEditDto getOrderCreateEditDto() {
        return OrderCreateEditDto.builder()
                .status(OrderStatus.UNPROCESSED)
                .orderName("Глинка/Вокал")
                .clientName("Андрей")
                .phone("8-924-989-59-04")
                .requestSource("Yandex")
                .createdAt(LocalDateTime.of(2023, 12, 15, 10, 15))
                .adminId(EXISTING_ADMIN_ID)
                .build();
    }
}
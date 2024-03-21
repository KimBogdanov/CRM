package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.Comment;
import ru.crm.system.database.entity.LogInfo;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class OrderServiceIT extends IntegrationTestBase {

    private static final Integer EXISTING_ORDER_ID = 1;
    private static final Integer NOT_EXISTING_ORDER_ID = 999;
    private static final Integer EXISING_ADMIN_ID = 1;

    private final OrderService orderService;
    private final OrderRepository orderRepository;

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

        existingOrder.ifPresent(order -> {
            assertThat(order.adminId()).isNull();
            assertThat(order.status()).isEqualTo(OrderStatus.UNPROCESSED);
        });

        var actualOrder = orderService.update(EXISTING_ORDER_ID, EXISING_ADMIN_ID, updateDto);

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

    @Test
    void changeStatus_shouldChangeStatus_ifOrderExists() {
        var existingOrder = orderService.findById(EXISTING_ORDER_ID);
        var actualOrder = orderService.changeStatus(EXISTING_ORDER_ID, EXISING_ADMIN_ID, OrderStatus.APPOINTMENT_COMPLETED);

        existingOrder.ifPresent(order ->
                assertAll(() -> assertThat(order.status()).isEqualTo(OrderStatus.UNPROCESSED)
                ));
        actualOrder.ifPresent(order -> assertAll(() ->
                assertThat(order.status()).isEqualTo(OrderStatus.APPOINTMENT_COMPLETED)));
    }

    @Test
    void addComment_shouldAddCommentAndLogInfo_ifOrderExists() {
        orderService.addComment(EXISTING_ORDER_ID, EXISING_ADMIN_ID, "Тестовый комментарий");
        var actualOrder = orderRepository.findById(EXISTING_ORDER_ID);

        List<Comment> comments;
        List<LogInfo> logInfos;

        if (actualOrder.isPresent()) {
            comments = actualOrder.get().getComments();
            logInfos = actualOrder.get().getLogInfos();

            assertThat(comments).hasSize(1);
            assertThat(comments.get(0).getText()).isEqualTo("Тестовый комментарий");

            assertThat(logInfos).hasSize(1);
            assertThat(logInfos.get(0).getDescription())
                    .isEqualTo("Админ Андрей Админов добавил комментарий к заявке №1: Тестовый комментарий");
        }
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
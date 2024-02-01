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
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.mapper.OrderCreateEditMapper;
import ru.crm.system.mapper.OrderReadMapper;
import ru.crm.system.service.OrderService;
import ru.crm.system.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Integer EXISTING_ORDER_ID = 1;
    private static final Integer NOT_EXISTING_ORDER_ID = 999;
    private static final Integer EXISING_ADMIN_ID = 1;

    @Mock
    private OrderCreateEditMapper orderCreateEditMapper;
    @Mock
    private OrderReadMapper orderReadMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private AdminRepository adminRepository;
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

    @Test
    void findById_shouldReturnOrder_whenOrderExists() {
        var orderEntity = getOrder();
        var orderReadDto = getOrderReadDto();
        when(orderRepository.findById(EXISTING_ORDER_ID)).thenReturn(Optional.of(orderEntity));
        when(orderReadMapper.map(orderEntity)).thenReturn(orderReadDto);

        var actualOrder = orderService.findById(EXISTING_ORDER_ID);

        assertThat(actualOrder).isPresent();
        actualOrder.ifPresent(order ->
                assertAll(() -> {
                    assertThat(order.id()).isEqualTo(EXISTING_ORDER_ID);
                    assertThat(order.status()).isEqualTo(OrderStatus.UNPROCESSED);
                    assertThat(order.orderName()).isEqualTo("Глинка/Вокал");
                    assertThat(order.clientName()).isEqualTo("Андрей");
                    assertThat(order.phone()).isEqualTo("8-924-989-59-04");
                    assertThat(order.requestSource()).isEqualTo("Yandex");
                    assertThat(order.createdAt()).isEqualTo("15-12-2023 10:15");
                }));
    }

    @Test
    void findById_shouldReturnEmpty_whenOrderNotExist() {
        when(orderRepository.findById(NOT_EXISTING_ORDER_ID)).thenReturn(Optional.empty());

        var actualOrder = orderService.findById(NOT_EXISTING_ORDER_ID);

        assertThat(actualOrder).isEmpty();
        verifyNoInteractions(orderReadMapper);
    }

    @Test
    void findAll_shouldReturnAllOrders_whenOrdersExist() {
        var orders = List.of(getOrder(), getOrder());
        when(orderRepository.findAll()).thenReturn(orders);

        var actualOrders = orderService.findAll();

        assertThat(actualOrders).hasSize(2);
        verify(orderReadMapper, times(2)).map(any(Order.class));
    }

    @Test
    void update_shouldUpdateExisting_whenOrderExists() {
        var orderEntity = getOrder();
        var updateDto = getOrderCreateEditDto();
        var orderReadDto = getOrderReadDto();

        when(orderRepository.findById(EXISTING_ORDER_ID)).thenReturn(Optional.of(orderEntity));
        when(orderCreateEditMapper.map(updateDto, orderEntity)).thenReturn(orderEntity);
        when(orderRepository.saveAndFlush(orderEntity)).thenReturn(orderEntity);
        when(orderReadMapper.map(orderEntity)).thenReturn(orderReadDto);

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
                    assertThat(order.requestSource()).isEqualTo("Yandex");
                }));
    }

    @Test
    void changeStatus_shouldChangeStatus_ifOrderExists() {
        var existingOrder = getOrder();
        existingOrder.setStatus(OrderStatus.APPOINTMENT_COMPLETED);
        when(orderRepository.findById(EXISTING_ORDER_ID)).thenReturn(Optional.of(existingOrder));
        when(orderReadMapper.map(existingOrder)).thenCallRealMethod();

        var actualOrder = orderService.changeStatus(EXISTING_ORDER_ID, EXISING_ADMIN_ID, OrderStatus.APPOINTMENT_COMPLETED);

        assertThat(actualOrder).isPresent();
        actualOrder.ifPresent(order -> assertThat(order.status()).isEqualTo(OrderStatus.APPOINTMENT_COMPLETED));
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
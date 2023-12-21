package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Comment;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.OrderCreateEditMapper;
import ru.crm.system.mapper.OrderReadMapper;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateEditMapper orderCreatedEditMapper;
    private final ApplicationEventPublisher publisher;
    private final OrderReadMapper orderReadMapper;
    private final AdminRepository adminRepository;

    @Transactional
    public OrderReadDto create(OrderCreateEditDto createDto) {
        return Optional.of(createDto)
                .map(orderCreatedEditMapper::map)
                .map(orderRepository::save)
                .map(order -> {
                    var logInfo = createLogInfo(order.getId());
                    logInfo.setDescription("Создана новая заявка");
                    logInfo.setAction(ActionType.CREATED);
                    publisher.publishEvent(new EntityEvent<>(order, AccessType.CREATE, logInfo));
                    return orderReadMapper.map(order);
                })
                .orElseThrow();
    }

    @Transactional
    public OrderReadDto changeStatus(Integer orderId,
                                     OrderStatus status,
                                     Integer adminId) {
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById)
                .map(order -> {
                    var logInfo = createLogInfo(order.getId());
                    setLogInfo(adminId, status, logInfo);
                    publisher.publishEvent(new EntityEvent<>(order, AccessType.CHANGE_STATUS, logInfo));
                    var maybeAdmin = adminRepository.findById(adminId);
                    maybeAdmin.ifPresent(order::setAdmin);
                    order.setStatus(status);
                    var changedOrder = orderRepository.saveAndFlush(order);
                    return orderReadMapper.map(changedOrder);
                }).orElseThrow();
    }

    public List<OrderReadDto> findAll() {
        return orderRepository.findAll().stream()
                .map(orderReadMapper::map)
                .toList();
    }

    public Optional<OrderReadDto> findById(Integer id) {
        return orderRepository.findById(id)
                .map(orderReadMapper::map);
    }

    @Transactional
    public Optional<OrderReadDto> update(Integer orderId,
                                         Integer adminId,
                                         OrderCreateEditDto editDto) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    if (order.getStatus() != editDto.status()) {
                        var logInfo = createLogInfo(orderId);
                        setLogInfo(adminId, editDto.status(), logInfo);
                        publisher.publishEvent(new EntityEvent<>(order, AccessType.CHANGE_STATUS, logInfo));
                    }
                    orderCreatedEditMapper.map(editDto, order);
                    return order;
                })
                .map(orderRepository::saveAndFlush)
                .map(orderReadMapper::map);
    }

    private LogInfoCreateDto createLogInfo(Integer orderId) {
        return LogInfoCreateDto.builder()
                .createdAt(now().truncatedTo(SECONDS))
                .orderId(orderId)
                .build();
    }

    private Comment createComment() {
        return Comment.of("Новый заказ", now().truncatedTo(SECONDS));
    }

    private ActionType getActionType(OrderStatus status) {
        return switch (status) {
            case APPOINTMENT_SCHEDULED, APPOINTMENT_COMPLETED -> ActionType.CHANGE_ORDER_STATUS;
            case SUCCESSFULLY_COMPLETED -> ActionType.SUCCESSFULLY_COMPLETED;
            case POOR_LEAD, REFUSED_TO_PURCHASE -> ActionType.COMPLETED_UNSUCCESSFULLY;
            case RESERVED -> ActionType.ARCHIVED;
            case UNPROCESSED -> ActionType.CREATED;
        };
    }

    private void setLogInfo(Integer adminId, OrderStatus status, LogInfoCreateDto logInfo) {
        var actionType = getActionType(status);
        logInfo.setAction(actionType);
        logInfo.setAdminId(adminId);
        logInfo.setDescription("Статус заявки изменён на: " + status.name());
    }
}
package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Comment;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.OrderCreateEditMapper;
import ru.crm.system.mapper.OrderReadMapper;

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

    @Transactional
    public OrderReadDto create(OrderCreateEditDto createDto) {
        return Optional.of(createDto)
                .map(orderCreatedEditMapper::map)
                .map(orderRepository::save)
                .map(order -> {
                    var logInfo = createLogInfo(order.getId());
                    logInfo.setAction(ActionType.CREATED);
                    publisher.publishEvent(new EntityEvent<>(order, AccessType.CREATE, logInfo));
                    return orderReadMapper.map(order);
                })
                .orElseThrow();
    }

    @Transactional
    public OrderReadDto changeStatus(Integer orderId,
                                     OrderStatus status,
                                     Integer adminId,
                                     String description) {
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById)
                .map(order -> {
                    var logInfo = createLogInfo(order.getId());
                    setLogInfo(adminId, description, logInfo);
                    publisher.publishEvent(new EntityEvent<>(order, AccessType.CHANGE_STATUS, logInfo));
                    order.setStatus(status);
                    order.addComment(createComment());
                    var changedOrder = orderRepository.saveAndFlush(order);
                    return orderReadMapper.map(changedOrder);
                }).orElseThrow();
    }

    private LogInfoCreateDto createLogInfo(Integer orderId) {
        return LogInfoCreateDto.builder()
                .createdAt(now())
                .orderId(orderId)
                .build();
    }

    private Comment createComment() {
        return Comment.of("Новый заказ", now().truncatedTo(SECONDS));
    }

    private static void setLogInfo(Integer adminId, String description, LogInfoCreateDto logInfo) {
        logInfo.setAction(ActionType.COMMENT);
        logInfo.setAdminId(adminId);
        logInfo.setDescription(description);
    }
}
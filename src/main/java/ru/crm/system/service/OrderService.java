package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.OrderCreateEditMapper;
import ru.crm.system.mapper.OrderReadMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateEditMapper orderCreatedEditMapper;
    private final ApplicationEventPublisher publisher;
    private final OrderReadMapper orderReadMapper;
    private final AdminRepository adminRepository;
    private final LogInfoService logInfoService;
    private final CommentService commentService;

    @Transactional
    public OrderReadDto create(OrderCreateEditDto createDto) {
        return Optional.of(createDto)
                .map(orderCreatedEditMapper::map)
                .map(orderRepository::save)
                .map(order -> {
                    var logInfo = logInfoService.creatLogInfoWhenNewOrderWasCreated(order.getId());
                    publisher.publishEvent(new EntityEvent<>(order, AccessType.CREATE, logInfo));
                    return orderReadMapper.map(order);
                })
                .orElseThrow();
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
                    if (order.getStatus() != editDto.getStatus()) {
                        var logInfo = logInfoService.createLogInfoWhenChangingOrderStatus(adminId, orderId);
                        logInfo.setAction(getActionType(editDto.getStatus()));
                        publisher.publishEvent(new EntityEvent<>(order, AccessType.CHANGE_STATUS, logInfo));
                    }
                    if (order.getAdmin() == null) {
                        editDto.setAdminId(adminId);
                    }
                    orderCreatedEditMapper.map(editDto, order);
                    return order;
                })
                .map(orderRepository::saveAndFlush)
                .map(orderReadMapper::map);
    }

    @Transactional
    public Optional<OrderReadDto> addComment(Integer orderId,
                                             Integer adminId,
                                             String text) {
        return adminRepository.findById(adminId)
                .map(admin -> orderRepository.findById(orderId).
                        map(order -> {
                            if (order.getStatus().equals(OrderStatus.UNPROCESSED)) {
                                order.setAdmin(admin);
                            }
                            var comment = commentService.createComment(text);
                            order.addComment(comment);
                            var logInfo = logInfoService.createLogInfoWhenAddingCommentToOrder(orderId, text, admin);
                            publisher.publishEvent(new EntityEvent<>(order, AccessType.UPDATE, logInfo));
                            return orderReadMapper.map(order);
                        })).flatMap(order -> order);
    }

    @Transactional
    public Optional<OrderReadDto> changeStatus(Integer orderId,
                                               Integer adminId,
                                               OrderStatus status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    var logInfo = logInfoService.createLogInfoWhenChangingOrderStatus(adminId, orderId);
                    logInfo.setAction(getActionType(status));
                    logInfo.setDescription(String.format("Статус заявки №%d изменён на: %s ", orderId, status));
                    publisher.publishEvent(new EntityEvent<>(order, AccessType.CHANGE_STATUS, logInfo));
                    return order;
                })
                .map(orderReadMapper::map);
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
}
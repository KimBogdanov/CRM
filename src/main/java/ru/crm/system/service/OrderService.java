package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.LogInfoCreateDto;
import ru.crm.system.dto.OrderCreateEditDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.OrderCreateEditMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateEditMapper orderCreatedEditMapper;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Integer create(OrderCreateEditDto createDto) {
        return Optional.of(createDto)
                .map(orderCreatedEditMapper::map)
                .map(orderRepository::save)
                .map(order -> {
                    var logInfo = createLogInfo(order.getId());
                    publisher.publishEvent(new EntityEvent<>(order, AccessType.CREATE, logInfo));
                    return order.getId();
                })
                .orElseThrow();
    }

    private LogInfoCreateDto createLogInfo(Integer id) {
        return LogInfoCreateDto.builder()
                .action(ActionType.CREATED)
                .createdAt(LocalDateTime.now())
                .orderId(id)
                .build();
    }
}
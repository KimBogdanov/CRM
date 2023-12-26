package ru.crm.system.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Order;
import ru.crm.system.dto.order.OrderCreateEditDto;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    @Override
    public Order map(OrderCreateEditDto createDto) {
        return Order.builder()
                .status(createDto.status())
                .orderName(createDto.orderName())
                .clientName(createDto.clientName())
                .phone(createDto.phone())
                .createdAt(LocalDateTime.now())
                .requestSource(createDto.requestSource())
                .build();
    }

    @Override
    public Order map(OrderCreateEditDto editDto, Order entity) {
        entity.setStatus(editDto.status());
        entity.setOrderName(editDto.orderName());
        entity.setClientName(editDto.clientName());
        entity.setPhone(editDto.phone());
        entity.setCreatedAt(editDto.createdAt());
        entity.setRequestSource(editDto.requestSource());
        return entity;
    }
}
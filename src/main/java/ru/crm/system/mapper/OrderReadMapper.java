package ru.crm.system.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Order;
import ru.crm.system.dto.OrderReadDto;

@RequiredArgsConstructor
@Component
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    @Override
    public OrderReadDto map(Order entity) {
        return OrderReadDto.builder()
                .status(entity.getStatus())
                .orderName(entity.getOrderName())
                .clientName(entity.getClientName())
                .phone(entity.getPhone())
                .requestSource(entity.getRequestSource())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
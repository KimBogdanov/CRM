package ru.crm.system.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Order;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.util.DateTimeUtil;

@RequiredArgsConstructor
@Component
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    @Override
    public OrderReadDto map(Order entity) {
        var dateTime = DateTimeUtil.formatLocalDateTime(entity.getCreatedAt());

        return OrderReadDto.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .orderName(entity.getOrderName())
                .clientName(entity.getClientName())
                .phone(entity.getPhone())
                .requestSource(entity.getRequestSource())
                .createdAt(dateTime)
                .adminId(entity.getAdmin() == null ? null : entity.getAdmin().getId())
                .build();
    }
}
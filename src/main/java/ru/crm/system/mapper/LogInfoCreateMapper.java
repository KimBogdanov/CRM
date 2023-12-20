package ru.crm.system.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.LogInfo;
import ru.crm.system.database.entity.Order;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.LogInfoCreateDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LogInfoCreateMapper implements Mapper<LogInfoCreateDto, LogInfo> {

    private final OrderRepository orderRepository;

    @Override
    public LogInfo map(LogInfoCreateDto createDto) {
        return LogInfo.builder()
                .actionType(createDto.action())
                .description(createDto.description())
                .createdAt(createDto.createdAt())
                .order(getOrder(createDto.orderId()))
                .build();
    }

    private Order getOrder(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(orderRepository::findById)
                .orElse(null);
    }
}
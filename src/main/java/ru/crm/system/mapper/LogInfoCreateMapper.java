package ru.crm.system.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Admin;
import ru.crm.system.database.entity.LogInfo;
import ru.crm.system.database.entity.Order;
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LogInfoCreateMapper implements Mapper<LogInfoCreateDto, LogInfo> {

    private final OrderRepository orderRepository;
    private final AdminRepository adminRepository;

    @Override
    public LogInfo map(LogInfoCreateDto createDto) {
        return LogInfo.builder()
                .actionType(createDto.getAction())
                .description(createDto.getDescription())
                .createdAt(createDto.getCreatedAt())
                .order(getOrder(createDto.getOrderId()))
                .admin(getAdmin(createDto.getAdminId()))
                .build();
    }

    private Order getOrder(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(orderRepository::findById)
                .orElse(null);
    }

    private Admin getAdmin(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(adminRepository::findById)
                .orElse(null);
    }
}
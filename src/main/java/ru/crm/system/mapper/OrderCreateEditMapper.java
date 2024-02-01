package ru.crm.system.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Admin;
import ru.crm.system.database.entity.Order;
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.dto.order.OrderCreateEditDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    private final AdminRepository adminRepository;

    @Override
    public Order map(OrderCreateEditDto createDto) {
        return Order.builder()
                .status(createDto.getStatus())
                .orderName(createDto.getOrderName())
                .clientName(createDto.getClientName())
                .phone(createDto.getPhone())
                .createdAt(LocalDateTime.now())
                .requestSource(createDto.getRequestSource())
                .build();
    }

    @Override
    public Order map(OrderCreateEditDto editDto, Order entity) {
        entity.setAdmin(getAdmin(editDto.getAdminId()));
        entity.setStatus(editDto.getStatus());
        entity.setOrderName(editDto.getOrderName());
        entity.setClientName(editDto.getClientName());
        entity.setPhone(editDto.getPhone());
        entity.setCreatedAt(editDto.getCreatedAt());
        entity.setRequestSource(editDto.getRequestSource());
        return entity;
    }

    private Admin getAdmin(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(adminRepository::findById)
                .orElse(null);
    }
}
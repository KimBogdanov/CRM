package ru.crm.system.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Admin;
import ru.crm.system.database.entity.Order;
import ru.crm.system.database.repository.AdminRepository;
import ru.crm.system.dto.OrderCreateEditDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    private final AdminRepository adminRepository;

    @Override
    public Order map(OrderCreateEditDto createDto) {
        return Order.builder()
                .status(createDto.status())
                .orderName(createDto.orderName())
                .clientName(createDto.clientName())
                .phone(createDto.phone())
                .status(createDto.status())
                .createdAt(LocalDateTime.now())
                .requestSource(createDto.requestSource())
                .admin(getAdmin(createDto.adminId()))
                .build();
    }

    private Admin getAdmin(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(adminRepository::findById)
                .orElse(null);
    }
}
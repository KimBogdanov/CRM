package ru.crm.system.dto.order;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.enums.OrderStatus;

import java.time.LocalDateTime;

@Builder
@FieldNameConstants
public record OrderReadDto(Integer id,
                           OrderStatus status,
                           String orderName,
                           String clientName,
                           String phone,
                           String requestSource,
                           LocalDateTime createdAt,
                           Integer adminId) {
}
package ru.crm.system.dto;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.enums.OrderStatus;

import java.time.LocalDateTime;

@Builder
@FieldNameConstants
public record OrderCreateEditDto(OrderStatus status,
                                 String orderName,
                                 String clientName,
                                 String phone,
                                 String requestSource,
                                 LocalDateTime createdAt,
                                 Integer adminId) {
}
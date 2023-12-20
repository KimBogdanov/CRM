package ru.crm.system.dto;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.enums.ActionType;

import java.time.LocalDateTime;

@Builder
@FieldNameConstants
public record LogInfoCreateDto(ActionType action,
                               String description,
                               LocalDateTime createdAt,
                               Integer orderId) {
}
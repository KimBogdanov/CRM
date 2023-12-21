package ru.crm.system.dto.loginfo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.enums.ActionType;

import java.time.LocalDateTime;

@Data
@Builder
@FieldNameConstants
public final class LogInfoCreateDto {

    private ActionType action;
    private String description;
    private LocalDateTime createdAt;
    private Integer orderId;
    private Integer adminId;
}
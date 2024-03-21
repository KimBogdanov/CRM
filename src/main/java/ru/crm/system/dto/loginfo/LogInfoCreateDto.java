package ru.crm.system.dto.loginfo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.enums.ActionType;

import java.time.LocalDateTime;

@Data
@Builder
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class LogInfoCreateDto {

    ActionType action;
    String description;
    LocalDateTime createdAt;
    Integer orderId;
    Integer adminId;
    Integer studentId;
    Integer teacherId;
    Integer lessonId;
}
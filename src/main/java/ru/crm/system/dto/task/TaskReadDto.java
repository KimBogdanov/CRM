package ru.crm.system.dto.task;

import lombok.Builder;
import ru.crm.system.database.entity.enums.ObjectNestedTask;
import ru.crm.system.database.entity.enums.TaskStatus;

import java.time.LocalDateTime;
@Builder
public record TaskReadDto(Integer id,
                          String description,
                          TaskStatus taskStatus,
                          LocalDateTime endDateTime,
                          ObjectNestedTask objectNestedTask) {
}

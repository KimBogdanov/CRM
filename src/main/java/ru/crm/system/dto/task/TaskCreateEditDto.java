package ru.crm.system.dto.task;

import lombok.Builder;
import ru.crm.system.database.entity.enums.ObjectNestedTask;
import ru.crm.system.database.entity.enums.TaskStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
public record TaskCreateEditDto(
        @NotBlank
        @Size(max = 255)
        String description,
        @NotBlank
        ObjectNestedTask objectNestedTask,
        Integer nestedObjectId,
        @NotNull
        LocalDateTime endDateTime,
        TaskStatus taskStatus
) {
}

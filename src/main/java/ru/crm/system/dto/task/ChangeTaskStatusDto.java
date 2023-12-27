package ru.crm.system.dto.task;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * DTO для изменения статуса одной задачи с фронта
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeTaskStatusDto {

    String object_name;

    String object_phone;

    String description;

    LocalDateTime endDateTime;
}
package ru.crm.system.dto.task;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO одной задачи с фронта для сохранения в БД
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddTaskDto {

    String object_name;

    String object_phone;

    String description;

    LocalDateTime endDateTime;
}
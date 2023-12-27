package ru.crm.system.dto.task;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO получения списка задач для выдачи на фронт
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetTaskDtoList {
    List<OneTaskDto> taskDtos;
}
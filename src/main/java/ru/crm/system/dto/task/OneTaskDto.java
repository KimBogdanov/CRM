package ru.crm.system.dto.task;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OneTaskDto {

    Long object_id;

    String object_name;

    String object_phone;

    String description;

    LocalDateTime createdDateTime;

    LocalDateTime endDateTime;
}
package ru.crm.system.dto.student;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.Subject;

import java.math.BigDecimal;

@Builder
@FieldNameConstants
public record StudentReadDto(Integer id,
                             String firstName,
                             String lastName,
                             String phone,
                             String email,
                             String avatar,
                             Subject subject,
                             BigDecimal balance) {
}
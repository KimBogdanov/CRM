package ru.crm.system.dto.order;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.enums.OrderStatus;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@FieldNameConstants
public record OrderCreateEditDto(OrderStatus status,

                                 @NotBlank
                                 String orderName,

                                 @NotBlank
                                 String clientName,

                                 @NotBlank
                                 String phone,

                                 String requestSource,

                                 LocalDateTime createdAt) {
}
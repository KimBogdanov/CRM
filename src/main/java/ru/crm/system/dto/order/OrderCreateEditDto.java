package ru.crm.system.dto.order;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.crm.system.database.entity.enums.OrderStatus;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Data
@FieldNameConstants
public class OrderCreateEditDto {

    private Integer adminId;

    private OrderStatus status;

    @NotBlank
    private String orderName;

    @NotBlank
    private String clientName;

    @NotBlank
    private String phone;

    private String requestSource;

    private LocalDateTime createdAt;
}
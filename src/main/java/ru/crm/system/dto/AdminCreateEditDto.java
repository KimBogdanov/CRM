package ru.crm.system.dto;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Builder
@FieldNameConstants
public record AdminCreateEditDto(String firstName,
                                 String lastName,
                                 String phone,
                                 String email,
                                 String rawPassword,
                                 MultipartFile avatar,
                                 BigDecimal shiftRate) {
}
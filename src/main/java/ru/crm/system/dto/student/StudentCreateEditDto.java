package ru.crm.system.dto.student;

import lombok.Builder;
import ru.crm.system.database.entity.Subject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
public record StudentCreateEditDto(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String phone,

        String email,

        String avatar,

        @NotNull
        Subject subject) {
}
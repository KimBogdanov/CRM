package ru.crm.system.dto.student;

import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
public record StudentCreateEditDto(

        @NotBlank
        @Size(max = 128)
        String firstName,

        @NotBlank
        @Size(max = 128)
        String lastName,

        @NotBlank
        String phone,

        @Email
        @Size(min = 3, max = 128)
        String email,

        String avatar,

        @NotNull
        String subject) {
}
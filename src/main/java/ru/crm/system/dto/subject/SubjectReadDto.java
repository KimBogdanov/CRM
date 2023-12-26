package ru.crm.system.dto.subject;

import lombok.Builder;
@Builder
public record SubjectReadDto(Integer id,
                             String name) {
}
package ru.crm.system.mapper.subject;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.dto.subject.SubjectReadDto;
import ru.crm.system.mapper.Mapper;

@Component
public class SubjectReadMapper implements Mapper<Subject, SubjectReadDto> {

    @Override
    public SubjectReadDto map(Subject entity) {
        return SubjectReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
package ru.crm.system.mapper.subject;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.dto.subject.SubjectCreateEditDto;
import ru.crm.system.mapper.Mapper;

@Component
public class SubjectCreateEditMapper implements Mapper<SubjectCreateEditDto, Subject> {

    @Override
    public Subject map(SubjectCreateEditDto createDto) {
        return Subject.builder()
                .name(createDto.name())
                .build();
    }
}
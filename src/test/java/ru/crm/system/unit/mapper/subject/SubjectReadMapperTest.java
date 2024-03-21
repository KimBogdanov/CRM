package ru.crm.system.unit.mapper.subject;

import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.dto.subject.SubjectReadDto;
import ru.crm.system.mapper.subject.SubjectReadMapper;

import static org.assertj.core.api.Assertions.assertThat;

class SubjectReadMapperTest {

    private final SubjectReadMapper subjectReadMapper = new SubjectReadMapper();

    @Test
    void map_shouldMapFromSubjectEntity_toSubjectReadDto() {
        var subject = getSubject();
        var expectedSubjectReadDto = getSubjectReadDto();

        var actualSubjectReadDto = subjectReadMapper.map(subject);

        assertThat(actualSubjectReadDto).isEqualTo(expectedSubjectReadDto);
    }

    private Subject getSubject() {
        return Subject.builder()
                .id(1)
                .name("Ударные")
                .build();
    }

    private SubjectReadDto getSubjectReadDto() {
        return SubjectReadDto.builder()
                .id(1)
                .name("Ударные")
                .build();
    }
}
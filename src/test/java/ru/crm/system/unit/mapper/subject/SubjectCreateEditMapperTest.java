package ru.crm.system.unit.mapper.subject;

import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.dto.subject.SubjectCreateEditDto;
import ru.crm.system.mapper.subject.SubjectCreateEditMapper;

import static org.assertj.core.api.Assertions.assertThat;

class SubjectCreateEditMapperTest {

    private final SubjectCreateEditMapper subjectCreateEditMapper = new SubjectCreateEditMapper();

    @Test
    void map_shouldMapFromSubjectCreateDto_toSubjectEntity() {
        var expectedSubject = getSubject();
        var subjectCreateDto = getSubjectCreateDto();
        var actualSubject = subjectCreateEditMapper.map(subjectCreateDto);

        assertThat(actualSubject).isEqualTo(expectedSubject);
    }

    private Subject getSubject() {
        return Subject.builder().name("Ударные").build();
    }

    private SubjectCreateEditDto getSubjectCreateDto() {
        return new SubjectCreateEditDto("Ударные");
    }
}
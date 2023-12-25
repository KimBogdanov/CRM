package ru.crm.system.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.dto.subject.SubjectDto;
import ru.crm.system.dto.subject.SubjectSaveDto;

import java.util.List;

/**
 * Класс для маппинга в дто и обратно
 * единичные сущности и list дто
 */
@Component
@AllArgsConstructor
public class SubjectMapper {

    public Subject fromDto(SubjectDto dto) {
        return Subject.builder()
                .id(dto.getId())
                .name(dto.getName())
                .students(dto.getStudents())
                .teachers(dto.getTeachers())
                .build();
    }

    public Subject fromDto(SubjectSaveDto dto) {
        return Subject.builder()
                .name(dto.getName())
                .students(dto.getStudents())
                .teachers(dto.getTeachers())
                .build();
    }

    public List<Subject> fromDto(List<SubjectDto> dtolist) {
        return dtolist.stream()
                .map(this::fromDto)
                .toList();
    }

    public SubjectDto toDto(Subject subject) {
        return SubjectDto.builder()
                .id(subject.getId())
                .name(subject.getName())
                .students(subject.getStudents())
                .teachers(subject.getTeachers())
                .build();
    }

    public List<SubjectDto> toDto(List<Subject> subjectList) {
        return subjectList.stream()
                .map(this::toDto)
                .toList();
    }
}
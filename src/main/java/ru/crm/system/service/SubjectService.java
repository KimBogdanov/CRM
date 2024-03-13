package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.repository.SubjectRepository;
import ru.crm.system.dto.subject.SubjectCreateEditDto;
import ru.crm.system.dto.subject.SubjectReadDto;
import ru.crm.system.exception.CreateEntityException;
import ru.crm.system.mapper.subject.SubjectCreateEditMapper;
import ru.crm.system.mapper.subject.SubjectReadMapper;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectCreateEditMapper subjectCreateEditMapper;
    private final SubjectReadMapper subjectReadMapper;

    public List<String> findAllSubjectNames() {
        return subjectRepository.findAll().stream()
                .map(Subject::getName)
                .toList();
    }

    public List<String> findAllByNameContaining(String name) {
        return subjectRepository.findAllByNameContaining(name).stream()
                .map(Subject::getName)
                .toList();
    }

    public Optional<SubjectReadDto> findById(Integer id) {
        return subjectRepository.findById(id)
                .map(subjectReadMapper::map);
    }

    public SubjectReadDto save(SubjectCreateEditDto createDto) {
        if (subjectRepository.existsByNameLikeIgnoreCase(createDto.name())) {
            throw new EntityExistsException("Предмет " + createDto.name() + " уже есть в базе данных");
        }
        return Optional.of(createDto)
                .map(subjectCreateEditMapper::map)
                .map(subjectRepository::save)
                .map(subjectReadMapper::map)
                .orElseThrow(() -> new CreateEntityException("Невозможно создать предмет с указанными параметрами"));
    }

    public boolean delete(Integer id) {
        return subjectRepository.findById(id)
                .map(subject -> {
                    subjectRepository.deleteById(id);
                    subjectRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.repository.TeacherRepository;
import ru.crm.system.dto.teacher.TeacherCreateEditDto;
import ru.crm.system.dto.teacher.TeacherReadDto;
import ru.crm.system.mapper.teacher.TeacherCreateEditMapper;
import ru.crm.system.mapper.teacher.TeacherReadMapper;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherReadMapper teacherReadMapper;
    private final TeacherCreateEditMapper teacherCreateEditMapper;

    public Optional<TeacherReadDto> findById(Integer id) {
        return teacherRepository.findById(id)
                .map(teacherReadMapper::map);
    }

    @Transactional
    public TeacherReadDto create(TeacherCreateEditDto createDto) {
        return Optional.of(createDto)
                .map(teacherCreateEditMapper::map)
                .map(teacherRepository::save)
                .map(teacherReadMapper::map)
                .orElseThrow(RuntimeException::new);
    }

    public List<TeacherReadDto> findAll() {
        return teacherRepository.findAll().stream()
                .map(teacherReadMapper::map)
                .toList();
    }

    public List<TeacherReadDto> getAllBySubject(Subject subject) {
        return teacherRepository.findAllBySubject(subject).stream()
                .map(teacherReadMapper::map)
                .toList();
    }
}
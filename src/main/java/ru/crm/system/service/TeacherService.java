package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.repository.TeacherRepository;
import ru.crm.system.dto.teacher.TeacherReadDto;
import ru.crm.system.mapper.teacher.TeacherReadMapper;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherReadMapper teacherReadMapper;

    public Optional<TeacherReadDto> findById(Integer id) {
        return teacherRepository.findById(id)
                .map(teacherReadMapper::map);
    }
}
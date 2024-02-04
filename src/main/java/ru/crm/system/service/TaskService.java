package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Task;
import ru.crm.system.database.repository.TaskRepository;
import ru.crm.system.dto.task.TaskCreateEditDto;
import ru.crm.system.dto.task.TaskReadDto;
import ru.crm.system.mapper.task.TaskCreateEditMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskCreateEditMapper taskCreateEditMapper;

    @Transactional
    public TaskReadDto create(TaskCreateEditDto createDto) {
        Task task = Optional.of(createDto)
                .map(taskCreateEditMapper::map)
                .orElseThrow();
        return null;
    }
}

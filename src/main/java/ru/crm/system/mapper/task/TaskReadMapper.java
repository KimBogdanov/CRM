package ru.crm.system.mapper.task;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Task;
import ru.crm.system.dto.task.TaskReadDto;
import ru.crm.system.mapper.Mapper;
@Component
public class TaskReadMapper implements Mapper<Task, TaskReadDto> {
    @Override
    public TaskReadDto map(Task task) {
        return TaskReadDto.builder()
                .description(task.getDescription())
                .taskStatus(task.getTaskStatus())
                .objectNestedTask(task.getObjectNestedTask())
                .endDateTime(task.getEndDateTime())
                .build();
    }
}

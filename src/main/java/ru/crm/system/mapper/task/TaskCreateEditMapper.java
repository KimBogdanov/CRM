package ru.crm.system.mapper.task;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Task;
import ru.crm.system.dto.task.TaskCreateEditDto;
import ru.crm.system.mapper.Mapper;
@Component
public class TaskCreateEditMapper implements Mapper<TaskCreateEditDto, Task> {
    @Override
    public Task map(TaskCreateEditDto dto) {
        return Task.builder()
                .description(dto.description())
                .taskStatus(dto.taskStatus())
                .objectNestedTask(dto.objectNestedTask())
                .endDateTime(dto.endDateTime())
                .nestedObjectId(dto.nestedObjectId())
                .build();
    }
}

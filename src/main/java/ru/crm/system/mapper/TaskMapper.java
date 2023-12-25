package ru.crm.system.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Task;
import ru.crm.system.dto.subject.SubjectDto;
import ru.crm.system.dto.subject.SubjectSaveDto;
import ru.crm.system.dto.task.GetTaskDtoList;
import ru.crm.system.dto.task.OneTaskDto;

import java.util.List;

/**
 * Класс для маппинга в дто и обратно
 * единичные сущности и list дто
 */
@Component
@AllArgsConstructor
public class TaskMapper {

    public Task fromDto(OneTaskDto dto) {
        return Task.builder()
                .id(dto.getObject_id())

                .build();
    }

}
package ru.crm.system.listener.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.BaseEntity;
import ru.crm.system.service.LogInfoService;

@Component
@RequiredArgsConstructor
public class EntityListener<T extends BaseEntity<Integer>> {

    private final LogInfoService logInfoService;

    @EventListener(condition = "#p0.accessType.name() == 'CREATE' or " +
            "#p0.accessType.name() == 'CHANGE_STATUS' or" +
            "#p0.accessType.name() == 'UPDATE'")
    public void logEntity(EntityEvent<T> event) {
        var createEditDto = event.getLogInfoCreateDto();
        logInfoService.create(createEditDto);
    }
}
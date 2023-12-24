package ru.crm.system.listener.entity;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.crm.system.database.entity.BaseEntity;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;

@Getter
public class EntityEvent<T extends BaseEntity<Integer>> extends ApplicationEvent {

    private final AccessType accessType;
    private final LogInfoCreateDto logInfoCreateDto;

    public EntityEvent(T entity, AccessType accessType, LogInfoCreateDto logInfoCreateDto) {
        super(entity);
        this.accessType = accessType;
        this.logInfoCreateDto = logInfoCreateDto;
    }
}
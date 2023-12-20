package ru.crm.system.listener.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Order;
import ru.crm.system.service.LogInfoService;

@Component
@RequiredArgsConstructor
public class EntityListener {

    private final LogInfoService logInfoService;

    @EventListener(condition = "#p0.accessType.name() == 'CREATE' or " +
            "#p0.accessType.name() == 'CHANGE_STATUS'")
    public void acceptEntityCreate(EntityEvent<Order> event) {
        var createEditDto = event.getLogInfoCreateDto();
        logInfoService.create(createEditDto);
    }
}
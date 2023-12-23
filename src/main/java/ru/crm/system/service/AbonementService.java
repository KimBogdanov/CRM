package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.repository.AbonementRepository;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.dto.abonement.AbonementReadDto;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.abonement.AbonementCreateEditMapper;
import ru.crm.system.mapper.abonement.AbonementReadMapper;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@Transactional
@RequiredArgsConstructor
public class AbonementService {

    private final AbonementRepository abonementRepository;
    private final AbonementCreateEditMapper abonementCreateEditMapper;
    private final AbonementReadMapper abonementReadMapper;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public AbonementReadDto create(AbonementCreatEditDto createDto,
                                   Integer adminId,
                                   Integer orderId) {
        return Optional.of(createDto)
                .map(abonementCreateEditMapper::map)
                .map(abonementRepository::save)
                .map(abonement -> {
                    var logInfo = createLogInfo(orderId);
                    logInfo.setAction(ActionType.SALE_OF_SUBSCRIPTION);
                    logInfo.setAdminId(adminId);
                    logInfo.setDescription(String.format("Продан абонемент на сумму %s руб. студенту с id %d",
                            createDto.balance(), createDto.studentId()));
                    publisher.publishEvent(new EntityEvent<>(abonement, AccessType.CREATE, logInfo));
                    return abonement;
                })
                .map(abonementReadMapper::map)
                .orElseThrow();
    }

    /**
     * Метод для создания базового LogInfo для всех методов
     */
    private LogInfoCreateDto createLogInfo(Integer orderId) {
        return LogInfoCreateDto.builder()
                .createdAt(now().truncatedTo(SECONDS))
                .orderId(orderId)
                .build();
    }
}
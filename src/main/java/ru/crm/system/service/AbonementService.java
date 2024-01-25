package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.database.repository.AbonementRepository;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.dto.abonement.AbonementReadDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.abonement.AbonementCreateEditMapper;
import ru.crm.system.mapper.abonement.AbonementReadMapper;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AbonementService {

    private final AbonementRepository abonementRepository;
    private final AbonementCreateEditMapper abonementCreateEditMapper;
    private final AbonementReadMapper abonementReadMapper;
    private final ApplicationEventPublisher publisher;
    private final LogInfoService logInfoService;

    public Optional<AbonementReadDto> findById(Integer id) {
        return abonementRepository.findById(id)
                .map(abonementReadMapper::map);
    }

    @Transactional
    public AbonementReadDto create(AbonementCreatEditDto createDto,
                                   Integer adminId,
                                   Integer orderId) {
        return Optional.of(createDto)
                .map(abonementCreateEditMapper::map)
                .map(abonementRepository::save)
                .map(abonement -> {
                    var orderLogInfo = logInfoService.createAbonementLogInfo(createDto, adminId, orderId);
                    publisher.publishEvent(new EntityEvent<>(abonement, AccessType.CREATE, orderLogInfo));
                    return abonement;
                })
                .map(abonementReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<AbonementReadDto> addMoneyIntoBalance(Integer adminId,
                                                          Integer abonementId,
                                                          BigDecimal moneyToAdd) {
        return abonementRepository.findById(abonementId)
                .map(abonement -> {
                    var newBalance = addMoney(moneyToAdd, abonement);
                    var logInfo = logInfoService.createAddMoneyLogInfo(adminId, moneyToAdd, abonement, newBalance);
                    publisher.publishEvent(new EntityEvent<>(abonement, AccessType.UPDATE, logInfo));
                    return abonement;
                })
                .map(abonementReadMapper::map);
    }

    private BigDecimal addMoney(BigDecimal moneyToAdd, Abonement abonement) {
        var balance = abonement.getBalance();
        var newBalance = balance.add(moneyToAdd);
        abonement.setBalance(newBalance);
        abonementRepository.flush();
        return newBalance;
    }
}
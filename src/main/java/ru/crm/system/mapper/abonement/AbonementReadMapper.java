package ru.crm.system.mapper.abonement;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.dto.abonement.AbonementReadDto;
import ru.crm.system.mapper.Mapper;

@Component
public class AbonementReadMapper implements Mapper<Abonement, AbonementReadDto> {

    @Override
    public AbonementReadDto map(Abonement entity) {
        return AbonementReadDto.builder()
                .id(entity.getId())
                .numberOfLessons(entity.getNumberOfLessons())
                .balance(entity.getBalance())
                .type(entity.getType().name())
                .begin(entity.getBegin())
                .expire(entity.getExpire())
                .status(entity.getStatus().name())
                .build();
    }
}
package ru.crm.system.mapper;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Admin;
import ru.crm.system.dto.AdminReadDto;

@Component
public class AdminReadMapper implements Mapper<Admin, AdminReadDto> {

    @Override
    public AdminReadDto map(Admin entity) {
        return AdminReadDto.builder()
                .id(entity.getId())
                .firstName(entity.getUserInfo().getFirstName())
                .lastName(entity.getUserInfo().getLastName())
                .phone(entity.getUserInfo().getPhone())
                .email(entity.getUserInfo().getEmail())
                .avatar(entity.getUserInfo().getAvatar())
                .shiftRate(entity.getShiftRate())
                .build();
    }
}
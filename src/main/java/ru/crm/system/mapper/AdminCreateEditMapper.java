package ru.crm.system.mapper;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Admin;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.dto.admin.AdminCreateEditDto;

@Component
public class AdminCreateEditMapper implements Mapper<AdminCreateEditDto, Admin> {

    @Override
    public Admin map(AdminCreateEditDto createDto) {
        var adminInfo = UserInfo.builder()
                .firstName(createDto.firstName())
                .lastName(createDto.lastName())
                .phone(createDto.phone())
                .email(createDto.email())
                .password(createDto.rawPassword())
                .role(Role.ADMIN)
                .build();

        return Admin.builder()
                .userInfo(adminInfo)
                .shiftRate(createDto.shiftRate())
                .build();
    }

}
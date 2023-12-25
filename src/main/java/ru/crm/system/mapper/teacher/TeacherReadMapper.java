package ru.crm.system.mapper.teacher;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.dto.teacher.TeacherReadDto;
import ru.crm.system.mapper.Mapper;

@Component
public class TeacherReadMapper implements Mapper<Teacher, TeacherReadDto> {

    @Override
    public TeacherReadDto map(Teacher teacher) {
        return TeacherReadDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getUserInfo().getFirstName())
                .lastName(teacher.getUserInfo().getLastName())
                .phone(teacher.getUserInfo().getPhone())
                .email(teacher.getUserInfo().getEmail())
                .avatar(teacher.getUserInfo().getAvatar())
                .status(teacher.getStatus())
                .payRatio(teacher.getPayRatio())
                .build();
    }
}
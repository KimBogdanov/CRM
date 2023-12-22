package ru.crm.system.mapper.student;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.dto.student.StudentCreateEditDto;
import ru.crm.system.mapper.Mapper;

@Component
public class StudentCreateEditMapper implements Mapper<StudentCreateEditDto, Student> {

    @Override
    public Student map(StudentCreateEditDto createDto) {
        var studentInfo = UserInfo.builder()
                .firstName(createDto.firstName())
                .lastName(createDto.lastName())
                .phone(createDto.phone())
                .email(createDto.email())
                .role(Role.STUDENT)
                .build();

        return Student.builder()
                .userInfo(studentInfo)
                .subject(createDto.subject())
                .build();
    }
}
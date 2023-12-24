package ru.crm.system.mapper.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.database.repository.SubjectRepository;
import ru.crm.system.dto.student.StudentCreateEditDto;
import ru.crm.system.mapper.Mapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudentCreateEditMapper implements Mapper<StudentCreateEditDto, Student> {

    private final SubjectRepository subjectRepository;

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
                .subject(getSubject(createDto.subject()))
                .build();
    }

    private Subject getSubject(String subjectName) {
        return Optional.of(subjectName)
                .flatMap(subjectRepository::getSubjectByName)
                .orElse(null);
    }
}
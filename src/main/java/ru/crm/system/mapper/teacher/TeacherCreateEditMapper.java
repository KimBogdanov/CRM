package ru.crm.system.mapper.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.database.entity.UserInfo;
import ru.crm.system.database.entity.enums.Role;
import ru.crm.system.database.entity.enums.TeacherStatus;
import ru.crm.system.database.repository.SubjectRepository;
import ru.crm.system.dto.teacher.TeacherCreateEditDto;
import ru.crm.system.mapper.Mapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeacherCreateEditMapper implements Mapper<TeacherCreateEditDto, Teacher> {

    private static final Double DEFAULT_PAY_RATIO = 1.0;

    private final SubjectRepository subjectRepository;

    @Override
    public Teacher map(TeacherCreateEditDto createDto) {
        var teacherInfo = UserInfo.builder()
                .firstName(createDto.firstName())
                .lastName(createDto.lastName())
                .phone(createDto.phone())
                .email(createDto.email())
                .password(createDto.rawPassword())
                .role(Role.TEACHER)
                .avatar(createDto.avatar())
                .build();
        return Teacher.builder()
                .userInfo(teacherInfo)
                .status(TeacherStatus.ACTIVE)
                .subjects(getSubjects(createDto.subjects()))
                .salaryPerHour(createDto.salaryPerHour())
                .payRatio(DEFAULT_PAY_RATIO)
                .build();
    }

    private List<Subject> getSubjects(List<String> subjectNames) {
        return subjectNames.stream()
                .map(subjectRepository::getSubjectByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
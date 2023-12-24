package ru.crm.system.mapper.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.repository.AbonementRepository;
import ru.crm.system.dto.student.StudentReadDto;
import ru.crm.system.mapper.Mapper;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class StudentReadMapper implements Mapper<Student, StudentReadDto> {

    private final AbonementRepository abonementRepository;

    @Override
    public StudentReadDto map(Student student) {
        return StudentReadDto.builder()
                .id(student.getId())
                .firstName(student.getUserInfo().getFirstName())
                .lastName(student.getUserInfo().getLastName())
                .phone(student.getUserInfo().getPhone())
                .email(student.getUserInfo().getEmail())
                .avatar(student.getUserInfo().getAvatar())
                .subject(student.getSubject().getName())
                .balance(getBalance(student.getId()))
                .build();
    }

    private BigDecimal getBalance(Integer studentId) {
        return abonementRepository.getBalanceByStudent(studentId);
    }
}
package ru.crm.system.mapper.abonement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.database.entity.AbonementType;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.repository.AbonementTypeRepository;
import ru.crm.system.database.repository.StudentRepository;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.mapper.Mapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AbonementCreateEditMapper implements Mapper<AbonementCreatEditDto, Abonement> {

    private final AbonementTypeRepository abonementTypeRepository;
    private final StudentRepository studentRepository;

    @Override
    public Abonement map(AbonementCreatEditDto creatDto) {
        return Abonement.builder()
                .numberOfLessons(creatDto.numberOfLessons())
                .balance(creatDto.balance())
                .type(getTypeByName(creatDto.type()))
                .begin(creatDto.begin())
                .expire(creatDto.expire())
                .status(creatDto.status())
                .student(getStudent(creatDto.studentId()))
                .build();
    }

    private AbonementType getTypeByName(String name) {
        return Optional.ofNullable(name)
                .map(abonementTypeRepository::getTypeByName)
                .orElse(null);
    }

    private Student getStudent(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(studentRepository::findById)
                .orElse(null);
    }
}
package ru.crm.system.mapper.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.database.repository.StudentRepository;
import ru.crm.system.database.repository.SubjectRepository;
import ru.crm.system.database.repository.TeacherRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.mapper.Mapper;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LessonCreateEditMapper implements Mapper<LessonCreateEditDto, Lesson> {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public Lesson map(LessonCreateEditDto createDto) {
        return Lesson.builder()
                .student(getStudent(createDto.studentId()))
                .teacher(getTeacher(createDto.teacherId()))
                .dateTime(createDto.lessonDateTime())
                .duration(createDto.duration())
                .subject(getSubject(createDto.subjectId()))
                .status(createDto.status())
                .type(createDto.type())
                .description(createDto.description())
                .cost(createDto.cost())
                .build();
    }

    private Student getStudent(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(studentRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Студена с номером %d не найден в базе.", id)));
    }

    private Teacher getTeacher(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(teacherRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Учитель с номером %d не найден в базе.", id)));
    }

    private Subject getSubject(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(subjectRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Предмет с номером %d не найден в базе.", id)));
    }
}
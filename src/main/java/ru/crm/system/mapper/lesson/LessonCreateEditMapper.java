package ru.crm.system.mapper.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;
import ru.crm.system.database.entity.enums.LessonStatus;
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
        var lesson = new Lesson();
        copyFromDtoToLesson(createDto, lesson);
        lesson.setStatus(LessonStatus.APPOINTED);
        return lesson;
    }

    @Override
    public Lesson map(LessonCreateEditDto editDto, Lesson entity) {
        copyFromDtoToLesson(editDto, entity);
        return entity;
    }

    private void copyFromDtoToLesson(LessonCreateEditDto createEditDto, Lesson lesson) {
        var students = studentRepository.findAllByFullNames(createEditDto.studentFullNames());
        lesson.setStudents(students);
        lesson.setTeacher(getTeacher(createEditDto.teacherFullName()));
        lesson.setDate(createEditDto.date());
        lesson.setTime(createEditDto.time());
        lesson.setDuration(createEditDto.duration());
        lesson.setSubject(getSubject(createEditDto.subject().getName()));
        lesson.setStatus(createEditDto.status());
        lesson.setPayType(createEditDto.payType());
        lesson.setLessonType(createEditDto.type());
        lesson.setDescription(createEditDto.description());
        lesson.setCost(createEditDto.cost());
    }

    private Teacher getTeacher(String teacherFullName) {
        return Optional.of(teacherFullName)
                .flatMap(teacherRepository::findByFullName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Учитель %s не найден в базе.", teacherFullName)));
    }

    private Subject getSubject(String subject) {
        return Optional.ofNullable(subject)
                .flatMap(subjectRepository::getSubjectByName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Предмет %s не найден в базе.", subject)));
    }
}
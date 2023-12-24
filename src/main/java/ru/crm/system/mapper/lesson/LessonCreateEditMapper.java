package ru.crm.system.mapper.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.Student;
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

    /**
     * Метод для маппинга из Create dto в сущность Lesson при создании нового урока.
     */
    @Override
    public Lesson map(LessonCreateEditDto createDto) {
        var lesson = new Lesson();
        copyFromDtoToLesson(createDto, lesson);
        lesson.setStatus(LessonStatus.APPOINTED);
        return lesson;
    }

    /**
     * Метод для обновления существующей сущности из переданного Edit dto.
     * Используется в методе update в {@link ru.crm.system.service.LessonService}
     */
    @Override
    public Lesson map(LessonCreateEditDto from, Lesson to) {
        copyFromDtoToLesson(from, to);
        to.setStatus(from.status());
        return to;
    }

    private Student getStudent(Integer id) {
        return Optional.ofNullable(id)
                .flatMap(studentRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Студент с номером %d не найден в базе.", id)));
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

    private void copyFromDtoToLesson(LessonCreateEditDto createEditDto, Lesson lesson) {
        lesson.setStudent(getStudent(createEditDto.studentId()));
        lesson.setTeacher(getTeacher(createEditDto.teacherId()));
        lesson.setDateTime(createEditDto.lessonDateTime());
        lesson.setDuration(createEditDto.duration());
        lesson.setSubject(getSubject(createEditDto.subjectId()));
        lesson.setType(createEditDto.type());
        lesson.setDescription(createEditDto.description());
        lesson.setCost(createEditDto.cost());
    }
}
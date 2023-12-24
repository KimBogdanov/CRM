package ru.crm.system.mapper.lesson;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.mapper.Mapper;

@Component
public class LessonReadMapper implements Mapper<Lesson, LessonReadDto> {

    @Override
    public LessonReadDto map(Lesson entity) {
        return LessonReadDto.builder()
                .id(entity.getId())
                .studentName(getFullStudentName(entity))
                .teacherName(getFullTeacherName(entity))
                .lessonDateTime(entity.getDateTime())
                .duration(entity.getDuration())
                .subject(entity.getSubject().getName())
                .status(entity.getStatus())
                .type(entity.getType())
                .description(entity.getDescription())
                .cost(entity.getCost())
                .build();
    }

    private String getFullStudentName(Lesson entity) {
        return entity.getStudent().getUserInfo().getFirstName()
               + " " +
               entity.getStudent().getUserInfo().getLastName();
    }

    private String getFullTeacherName(Lesson entity) {
        return entity.getTeacher().getUserInfo().getFirstName()
               + " " +
               entity.getStudent().getUserInfo().getLastName();
    }

}

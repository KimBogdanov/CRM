package ru.crm.system.mapper.lesson;

import org.springframework.stereotype.Component;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.mapper.Mapper;

import java.util.List;

@Component
public class LessonReadMapper implements Mapper<Lesson, LessonReadDto> {

    @Override
    public LessonReadDto map(Lesson entity) {
        return LessonReadDto.builder()
                .id(entity.getId())
                .studentFullNames(getStudentFullNames(entity))
                .teacherFullName(getFullTeacherName(entity))
                .lessonDate(entity.getDate())
                .lessonTime(entity.getTime())
                .duration(entity.getDuration())
                .subject(entity.getSubject().getName())
                .status(entity.getStatus())
                .type(entity.getLessonType())
                .payType(entity.getPayType())
                .description(entity.getDescription())
                .cost(entity.getCost())
                .build();
    }

    private List<String> getStudentFullNames(Lesson lesson) {
        return lesson.getStudents().stream()
                .map(student -> String.format("%s %s",
                        student.getUserInfo().getFirstName(),
                        student.getUserInfo().getLastName()))
                .toList();
    }

    private String getFullTeacherName(Lesson entity) {
        return String.format("%s %s",
                entity.getTeacher().getUserInfo().getFirstName(),
                entity.getTeacher().getUserInfo().getLastName());
    }
}
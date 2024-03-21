package ru.crm.system.database.repository;

import ru.crm.system.database.entity.Lesson;
import ru.crm.system.dto.filter.LessonFilter;

import java.util.List;

public interface LessonFilterRepository {

    List<Lesson> findAllByFilter(LessonFilter lessonFilter);
}
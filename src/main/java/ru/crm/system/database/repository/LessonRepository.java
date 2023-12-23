package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
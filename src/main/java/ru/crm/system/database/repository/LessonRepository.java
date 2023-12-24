package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.enums.LessonStatus;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Modifying
    @Query("""
            update Lesson l
            set l.status = :status
            """)
    void changeLessonStatus(LessonStatus status);
}
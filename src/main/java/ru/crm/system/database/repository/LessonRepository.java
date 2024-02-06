package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Query("""
            select l
            from Lesson l
            where l.date = :date and l.time = :time
            """)
    Optional<Lesson> findByDataAndTime(LocalDate date, LocalTime time);
}
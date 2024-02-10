package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.LogInfo;

import java.util.List;

public interface LogInfoRepository extends JpaRepository<LogInfo, Integer> {

    @Query("""
            select li
            from LogInfo li
            where li.lesson.id = :id
            """)
    List<LogInfo> findByLessonId(Integer id);
}
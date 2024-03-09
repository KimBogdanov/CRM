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
    List<LogInfo> findAllByLessonId(Integer id);

    @Query("""
            select li
            from LogInfo li
            where li.teacher.id = :id
            """)
    List<LogInfo> findAllByTeacherId(Integer id);

    @Query("""
            select li
            from LogInfo li
            where li.student.id in :ids
            """)
    List<LogInfo> findAllByStudentIds(Integer... ids);
}
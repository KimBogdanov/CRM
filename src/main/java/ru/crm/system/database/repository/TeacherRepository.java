package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("""
            select th from Teacher th
            join th.subjects sj
            where sj = :subject
            """)
    List<Teacher> findAllBySubject(@Param("subject") Subject subject);

    @Query("""
            select t
            from Teacher t
            where concat(t.userInfo.firstName, ' ', t.userInfo.lastName) = :fullName
            """)
    Optional<Teacher> findByFullName(String fullName);
}
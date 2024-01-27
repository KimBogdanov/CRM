package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.Subject;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("""
            select Student from Student
            left join Subject on Student.subject
            where Subject.name =:subject
            """)
    List<Student> findAllBySubject(Subject subject);
}
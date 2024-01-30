package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("""
            select st from Student st
            left join Subject sj on st.subject.id = sj.id
            where sj.name =:subject
            """)
    List<Student> findAllBySubject(String subject);
}
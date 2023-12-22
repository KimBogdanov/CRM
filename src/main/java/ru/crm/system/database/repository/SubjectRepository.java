package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query("""
            select s
            from Subject s
            where s.name = :name
            """)
    Subject getSubjectByName(String name);
}
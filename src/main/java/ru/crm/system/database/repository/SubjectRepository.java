package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    List<Subject> findAllByNameContaining(String name);

    boolean existsByNameLikeIgnoreCase(String subjectName);

    @Query("""
            select s
            from Subject s
            where s.name = :name
            """)
    Optional<Subject> getSubjectByName(String name);
}
package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}
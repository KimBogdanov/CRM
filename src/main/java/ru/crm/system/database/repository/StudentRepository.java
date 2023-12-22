package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
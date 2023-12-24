package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
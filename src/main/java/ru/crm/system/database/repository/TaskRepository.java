package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Task;
import ru.crm.system.database.entity.Teacher;

public interface TaskRepository extends JpaRepository<Task, Integer> {

}
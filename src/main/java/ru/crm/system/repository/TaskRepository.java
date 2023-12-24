package ru.crm.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    /**
     * Выдает список задач
     * @return список задач с сортировкой по дате окончания
     */
    List<Task> findAllByEndDateTime();

}

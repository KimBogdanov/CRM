package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.crm.system.database.entity.Task;
import ru.crm.system.database.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * Возвращает список всех задач
     * @return список всех задач
     */
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
//        вытащить данные для task из сущностей учитель заказ и студент
//        for (Task taks :tasks) {
//            task.
//        }
//        return tasks;
        return null;
    }
}

package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.entity.Task;
import ru.crm.system.repository.SubjectRepository;
import ru.crm.system.repository.TaskRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
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
        List<Task> tasks = taskRepository.findAllByEndDateTime();
//        вытащить данные для task из сущностей учитель заказ и студент
//        for (Task taks :tasks) {
//            task.
//        }
//        return tasks;
        return null;
    }
}

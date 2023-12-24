package ru.crm.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.crm.system.database.entity.Task;
import ru.crm.system.dto.task.GetTaskDtoList;
import ru.crm.system.dto.task.TaskDto;
import ru.crm.system.dto.task.TaskSaveDto;
import ru.crm.system.mapper.TaskMapper;
import ru.crm.system.service.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
public class TaskApi {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Получить весь список задач
     * @return список задач
     */
    @GetMapping()
    public GetTaskDtoList getAll() {
        List<Task> tasks = taskService.getAll();

        return tasks;
    }

//    /**
//     * Сохраняет задачу в БД
//     *
//     * @param saveDto DTO задачи
//     * @return сохраненное dto задачи
//     */
//    @PostMapping
//    public TaskDto save(@RequestBody TaskSaveDto saveDto) {
//        Task task = taskMapper.fromDto(saveDto);
//        Task save = taskService.save(task);
//        return taskMapper.toDto(save);
//    }
//
//
//    @DeleteMapping("{id}")
//    public void delete(@PathVariable Integer id) {
//        taskService.delete(id);
//    }
}
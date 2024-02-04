package ru.crm.system.http.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.crm.system.dto.task.TaskCreateEditDto;
import ru.crm.system.dto.task.TaskReadDto;
import ru.crm.system.service.TaskService;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskRestController {
    private final TaskService taskService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Method to create task.")
    public TaskReadDto create(@Validated @RequestBody TaskCreateEditDto createDto) {
        return taskService.create(createDto);
    }
}

package ru.crm.system.http.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.exception.CreateEntityException;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.service.LessonService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonRestController {

    private final LessonService lessonService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get lesson by id")
    public LessonReadDto findById(@PathVariable Integer id) {
        return lessonService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Урок с номером %d не найден.", id)));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new lesson and save it in database")
    public LessonReadDto create(@RequestBody LessonCreateEditDto lessonCreateEditDto) {
        return lessonService.create(lessonCreateEditDto)
                .orElseThrow(() -> new CreateEntityException("При создании урока произошла ошибка. Проверьте входные данные."));
    }

    @PatchMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update existing lesson")
    public LessonReadDto update(@PathVariable("id") Integer lessonId,
                                @RequestBody LessonCreateEditDto updateDto) {
        return lessonService.update(lessonId, updateDto)
                .orElseThrow(() -> new NotFoundException(String.format("Урок с номером %d не найден.", lessonId)));
    }

    @PatchMapping("/{id}/change-status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change lesson status. The main method in app))")
    public LessonReadDto changeLessonStatus(@PathVariable("id") Integer lessonId,
                                            LessonStatus status) {
        return lessonService.changeLessonStatus(lessonId, status)
                .orElseThrow(() -> new NotFoundException(String.format("Урок с номером %d не найден.", lessonId)));
    }

    @PostMapping("/{id}/add-comment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add comment to the lesson")
    public LessonReadDto addComment(@PathVariable("id") Integer lessonId,
                                    String text) {
        return lessonService.addComment(lessonId, text)
                .orElseThrow(() -> new NotFoundException(String.format("Урок с номером %d не найден.", lessonId)));
    }

    @GetMapping("/lesson-statuses")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all possible statuses for lesson. Use in changeLessonStatus().")
    public List<String> getStatuses() {
        return Arrays.stream(LessonStatus.values())
                .map(LessonStatus::name)
                .toList();
    }
}
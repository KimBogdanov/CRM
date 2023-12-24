package ru.crm.system.http.rest;

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
import org.springframework.web.server.ResponseStatusException;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.service.LessonService;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonRestController {

    private final LessonService lessonService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LessonReadDto findById(@PathVariable Integer id) {
        return lessonService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonReadDto create(@RequestBody LessonCreateEditDto lessonCreateEditDto,
                                Integer adminId) {
        return lessonService.create(lessonCreateEditDto, adminId);
    }

    @PatchMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public LessonReadDto update(@PathVariable("id") Integer lessonId,
                                @RequestBody LessonCreateEditDto updateDto) {
        return lessonService.update(lessonId, updateDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/change-status")
    @ResponseStatus(HttpStatus.OK)
    public LessonReadDto changeLessonStatus(@PathVariable("id") Integer lessonId,
                                            LessonStatus status) {
        return lessonService.changeLessonStatus(lessonId, status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/add-comment")
    @ResponseStatus(HttpStatus.OK)
    public LessonReadDto addComment(@PathVariable("id") Integer lessonId,
                                    String text) {
        return lessonService.addComment(lessonId, text)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
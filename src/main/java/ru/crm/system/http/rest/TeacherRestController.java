package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.crm.system.dto.teacher.TeacherCreateEditDto;
import ru.crm.system.dto.teacher.TeacherReadDto;
import ru.crm.system.service.TeacherService;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherRestController {

    private final TeacherService teacherService;

    @GetMapping("/{id})")
    @ResponseStatus(HttpStatus.OK)
    public TeacherReadDto findById(@PathVariable Integer id) {
        return teacherService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherReadDto create(TeacherCreateEditDto createDto) {
        return teacherService.create(createDto);
    }
}
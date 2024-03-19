package ru.crm.system.http.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.dto.teacher.TeacherCreateEditDto;
import ru.crm.system.dto.teacher.TeacherReadDto;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherRestController {

    private final TeacherService teacherService;

    @GetMapping("/{id})")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get teacher by id.")
    public TeacherReadDto findById(@PathVariable Integer id) {
        return teacherService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Учитель с номером %d не найден.", id)));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Method to create new teacher.")
    public TeacherReadDto create(TeacherCreateEditDto createDto) {
        return teacherService.create(createDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all teachers from database.")
    public List<TeacherReadDto> findAll() {
        return teacherService.findAll();
    }

    @GetMapping("/by-subject")
    public List<TeacherReadDto> findBySubject(Subject subject) {
        return teacherService.getAllBySubject(subject);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestParam Integer id) {
        if (!teacherService.delete(id)) {
            throw new NotFoundException("Учитель с id " + id + " не найден в базе данных");
        }
        return true;
    }
}
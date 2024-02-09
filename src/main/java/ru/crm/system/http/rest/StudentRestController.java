package ru.crm.system.http.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.dto.student.StudentCreateEditDto;
import ru.crm.system.dto.student.StudentReadDto;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get student by id")
    public StudentReadDto findById(@PathVariable Integer id) {
        return studentService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Студент с номером %d не найден.", id)));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Method to create teacher.")
    public StudentReadDto create(Integer orderId,
                                 Integer adminId,
                                 @Validated @RequestBody StudentCreateEditDto createDto) {
        return studentService.create(orderId, createDto, adminId);
    }

    @GetMapping("/by-subject")
    @Operation(summary = "Method to find students by subject.")
    public ResponseEntity<List<StudentReadDto>> findAllBySubject(@RequestParam String subject) {
        var studentsBySubject = studentService.findAllBySubject(subject);
        return ResponseEntity.ok().body(studentsBySubject);
    }
}
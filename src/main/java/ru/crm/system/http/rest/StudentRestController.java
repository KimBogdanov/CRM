package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.dto.student.StudentCreateEditDto;
import ru.crm.system.dto.student.StudentReadDto;
import ru.crm.system.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentReadDto create(Integer orderId,
                                 Integer adminId,
                                 @Validated @RequestBody StudentCreateEditDto createDto) {
        return studentService.create(orderId, createDto, adminId);
    }
}
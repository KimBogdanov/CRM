package ru.crm.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.dto.subject.SubjectCreateEditDto;
import ru.crm.system.dto.subject.SubjectReadDto;
import ru.crm.system.mapper.SubjectMapper;
import ru.crm.system.service.SubjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subjects")
public class SubjectApi {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    /**
     * Возвращает dto предмета по ID предмета
     *
     * @param id ID предмета
     * @return dto предмета
     */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get subject by id")
    public SubjectReadDto getById(@PathVariable Integer id) {
        return subjectService.getById(id);
//        Subject subject = subjectService.getById(id);
//        SubjectDto subjectDto = subjectMapper.toDto(subject);
//        return subjectDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of all subjects from db")
    public List<SubjectReadDto> findAll() {
        return subjectService.getAll();
    }

    //    /**
//     * Сохраняет предмет в БД
//     *
//     * @param saveDto DTO предмета
//     * @return сохраненное dto предмета
//     */
//    @PostMapping
//    public SubjectDto save(@RequestBody SubjectSaveDto saveDto) {
//        Subject subject = subjectMapper.fromDto(saveDto);
//        Subject save = subjectService.save(subject);
//        return subjectMapper.toDto(save);
//    }
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new subject")
    public SubjectReadDto create(@RequestBody SubjectCreateEditDto createDto) {
        return subjectService.save(createDto);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete subject by id")
    public void delete(@PathVariable Integer id) {
        subjectService.delete(id);
    }
}
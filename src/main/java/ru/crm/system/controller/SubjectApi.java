package ru.crm.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.dto.subject.SubjectDto;
import ru.crm.system.dto.subject.SubjectSaveDto;
import ru.crm.system.mapper.SubjectMapper;
import ru.crm.system.service.SubjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/subjects")
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
    public SubjectDto getById(@PathVariable Integer id) {
        Subject subject = subjectService.getById(id);
        SubjectDto subjectDto = subjectMapper.toDto(subject);
        return subjectDto;
    }

    /**
     * Сохраняет предмет в БД
     *
     * @param saveDto DTO предмета
     * @return сохраненное dto предмета
     */
    @PostMapping
    public SubjectDto save(@RequestBody SubjectSaveDto saveDto) {
        Subject subject = subjectMapper.fromDto(saveDto);
        Subject save = subjectService.save(subject);
        return subjectMapper.toDto(save);
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        subjectService.delete(id);
    }
}
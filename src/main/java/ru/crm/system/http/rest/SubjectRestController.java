package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.dto.subject.SubjectReadDto;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.service.SubjectService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectRestController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<String>> findAllSubjectNames() {
        var subjectNames = subjectService.findAllSubjectNames();

        return ok().body(subjectNames);
    }

    @GetMapping("/by-name")
    public ResponseEntity<SubjectReadDto> findByName(@RequestParam String name) {
        return subjectService.findByName(name)
                .map(subject -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(subject)
                ).orElseThrow(() -> new NotFoundException("Предмет с названием " + name + " не найден в базе данных."));
    }
}
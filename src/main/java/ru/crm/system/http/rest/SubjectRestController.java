package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectRestController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<String>> findAllSubjectNames() {
        var subjectNames = subjectService.findAllSubjectNames();

        return ResponseEntity
                .ok()
                .body(subjectNames);
    }
}
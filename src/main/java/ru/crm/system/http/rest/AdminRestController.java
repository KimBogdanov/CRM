package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.crm.system.dto.admin.AdminCreateEditDto;
import ru.crm.system.dto.admin.AdminReadDto;
import ru.crm.system.service.AdminService;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminService adminService;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminReadDto create(@RequestBody AdminCreateEditDto createDto) {
        return adminService.create(createDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdminReadDto findById(@PathVariable("id") Integer id) {
        return adminService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
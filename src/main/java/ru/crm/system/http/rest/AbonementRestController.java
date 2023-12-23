package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.dto.abonement.AbonementReadDto;
import ru.crm.system.service.AbonementService;

@RestController
@RequestMapping("/api/v1/abonements")
@RequiredArgsConstructor
public class AbonementRestController {

    private final AbonementService abonementService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AbonementReadDto create(@RequestBody AbonementCreatEditDto createDto,
                                   Integer adminId,
                                   Integer orderId) {
        return abonementService.create(createDto, adminId, orderId);
    }
}
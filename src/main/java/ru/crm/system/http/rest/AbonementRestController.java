package ru.crm.system.http.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.dto.abonement.AbonementReadDto;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.service.AbonementService;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/abonements")
@RequiredArgsConstructor
public class AbonementRestController {

    private final AbonementService abonementService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an abonement by id")
    public AbonementReadDto findById(@PathVariable Integer id) {
        return abonementService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Абонемент с номером %d не найден.", id)));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new user and save it in db")
    public AbonementReadDto create(@RequestBody AbonementCreatEditDto createDto,
                                   Integer adminId,
                                   Integer orderId) {
        return abonementService.create(createDto, adminId, orderId);
    }

    @PatchMapping("/{adminId}/{abonementId}/add-money")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add money into student's account by abonement id")
    public AbonementReadDto addMoney(@PathVariable("adminId") Integer adminId,
                                     @PathVariable("abonementId") Integer abonementId,
                                     @Validated @Positive @RequestParam BigDecimal moneyToAdd) {
        return abonementService.addMoneyIntoBalance(adminId, abonementId, moneyToAdd)
                .orElseThrow(() -> new NotFoundException(String.format("Абонемент с номером %d не найден.", abonementId)));
    }
}
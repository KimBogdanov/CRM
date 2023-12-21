package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderReadDto create(@Validated @RequestBody OrderCreateEditDto createDto) {
        return orderService.create(createDto);
    }
}
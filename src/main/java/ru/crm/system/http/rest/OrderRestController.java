package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.service.OrderService;

import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderReadDto> getAll() {
        return orderService.findAll();
    }

    /**
     * Метод для получения всех возможных статусов заказа,
     * чтобы было можно менять статус при перетаскивании заказа в окне заказов.
     */
    @GetMapping("/statuses")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderStatus> getStatuses() {
        return List.of(OrderStatus.values());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderReadDto findById(@PathVariable("id") Integer id) {
        return orderService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{id}/change-status")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderReadDto changeStatus(@PathVariable("id") Integer orderId,
                                     OrderStatus status,
                                     Integer adminId) {
        return orderService.changeStatus(orderId, status, adminId);
    }

    @PatchMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public OrderReadDto update(@PathVariable("id") Integer orderId,
                               Integer adminId,
                               @RequestBody OrderCreateEditDto editDto) {
        return orderService.update(orderId, adminId, editDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
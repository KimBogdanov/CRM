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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.dto.order.OrderCreateEditDto;
import ru.crm.system.dto.order.OrderReadDto;
import ru.crm.system.exception.NotFoundException;
import ru.crm.system.service.OrderService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Method to create new order.")
    public OrderReadDto create(@Validated @RequestBody OrderCreateEditDto createDto) {
        return orderService.create(createDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Method to get all orders from database.")
    public List<OrderReadDto> getAll() {
        return orderService.findAll();
    }

    /**
     * Метод для получения всех возможных статусов заказа,
     * чтобы было можно менять статус при перетаскивании заказа в окне заказов.
     */
    @GetMapping("/statuses")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Method to get all possible statuses for oder. Use in POST update() and changeStatus()")
    public List<String> getStatuses() {
        return Arrays.stream(OrderStatus.values())
                .map(OrderStatus::name)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get order by id")
    public OrderReadDto findById(@PathVariable("id") Integer id) {
        return orderService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Заказ с номером %d не найден.", id)));
    }

    @PatchMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Method to update existing order.")
    public OrderReadDto update(@PathVariable("id") Integer orderId,
                               Integer adminId,
                               @RequestBody OrderCreateEditDto editDto) {
        return orderService.update(orderId, adminId, editDto)
                .orElseThrow(() -> new NotFoundException(String.format("Заказ с номером %d не найден.", orderId)));
    }

    @PostMapping("/{id}/add-comment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add comment to order.")
    public OrderReadDto addComment(@PathVariable("id") Integer orderId,
                                   Integer adminId,
                                   String text) {
        return orderService.addComment(orderId, adminId, text)
                .orElseThrow(() -> new NotFoundException(String.format("Заказ с номером %d не найден.", orderId)));
    }

    @PatchMapping("/{id}/change-status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Method to change order status by drag-and-drop.")
    public OrderReadDto changeStatus(@PathVariable("id") Integer orderId,
                                     Integer adminId,
                                     OrderStatus status) {
        return orderService.changeStatus(orderId, adminId, status)
                .orElseThrow(() -> new NotFoundException(String.format("Заказ с номером %d не найден.", orderId)));
    }
}
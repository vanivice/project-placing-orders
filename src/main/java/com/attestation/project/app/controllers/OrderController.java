package com.attestation.project.app.controllers;

import com.attestation.project.app.model.dto.request.OrderInfoRequest;
import com.attestation.project.app.model.dto.response.OrderInfoResponse;
import com.attestation.project.app.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "заказ")

public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Создать заказ")
    public OrderInfoResponse createOrder(@RequestBody @Valid OrderInfoRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/get-my-order")
    @Operation(summary = "Получить список своих заказов")
    public List<OrderInfoResponse> getMyOrder() {
        return orderService.getMyOrder();
    }

    @GetMapping("/get/{number}")
    @Operation(summary = "Получить информацию по заказу по номеру заказа")
    public OrderInfoResponse getOrder(@PathVariable String number) {
        return orderService.getCustomerOrder(number);
    }


    @PutMapping("/update/{number}")
    @Operation(summary = "Обновить данные по заказу по номеру заказа")
    public OrderInfoResponse updateMyOrder(@PathVariable String number, @RequestBody OrderInfoResponse request) {
        return orderService.updateMyOrder(number, request);
    }

    @DeleteMapping("/delete/{number}")
    @Operation(summary = "Удалить заказ")
    public OrderInfoResponse deleteMyOrder(@PathVariable String number) {
        return orderService.deleteMyOrder(number);
    }
}

package com.attestation.project.app.controllers;

import com.attestation.project.app.model.db.entity.Customer;
import com.attestation.project.app.model.dto.request.CustomerRequest;
import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "пользователи")

public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/my-info")
    @Operation(summary = "Получить информацию о себе")
    public Customer getLiveCustomer() {
        return customerService.getCurrentUser();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Удалить профиль")
    public CustomerResponse deleteUser() {
        return customerService.deleteUser();
    }

    @PutMapping("/update")
    @Operation(summary = "Обновить данные профиля")
    public CustomerResponse updateCustomer(@RequestBody CustomerRequest req) {
        return customerService.updateCustomer(req);
    }

    @GetMapping("/get-ex")
    @Operation(summary = "Стать исполнителем")
    public CustomerResponse getExecutor() {
        return customerService.getRoleExecutor();
    }
}

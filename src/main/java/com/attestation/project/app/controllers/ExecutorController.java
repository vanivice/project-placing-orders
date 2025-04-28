package com.attestation.project.app.controllers;

import com.attestation.project.app.model.dto.request.ExecutorRequest;
import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.model.dto.response.ExecutorResponse;
import com.attestation.project.app.service.ExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/executor")
@RequiredArgsConstructor
@Tag(name = "каталог исполнителей")
public class ExecutorController {
    private final ExecutorService executorService;

    @PutMapping("/update")
    @Operation(summary = "Обновить данные о себе")
    public ExecutorResponse updateExecutor(@RequestBody ExecutorRequest req) {
        return executorService.updateExecutor(req);
    }

    @GetMapping("/get-cus")
    @Operation(summary = "Удалить статус исполнителя")
    public CustomerResponse getCustomer() {
        return executorService.getCustomer();
    }
}

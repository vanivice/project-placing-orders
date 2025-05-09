package com.attestation.project.app.controllers;

import com.attestation.project.app.model.dto.request.ExecutorRequest;
import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.model.dto.response.ExecutorResponse;
import com.attestation.project.app.service.ExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/executor")
@RequiredArgsConstructor
@Tag(name = "каталог исполнителей")
public class ExecutorController {
    private final ExecutorService executorService;

    @PutMapping("/update")
    @PreAuthorize("hasRole('EXECUTOR')")
    @Operation(summary = "Обновить данные о себе")
    public ExecutorResponse updateExecutor(@RequestBody ExecutorRequest req) {
        return executorService.updateExecutor(req);
    }

    @GetMapping("/get-cus")
    @PreAuthorize("hasRole('EXECUTOR')")
    @Operation(summary = "Удалить статус исполнителя")
    public CustomerResponse getCustomer() {
        return executorService.getCustomer();
    }

    @GetMapping("/get-all")
    @Operation(summary = "Посмотерть список исполнителей")
    public Page<ExecutorResponse> getAllCatalog(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer perPage,
                                                    @RequestParam(defaultValue = "id") String sort,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                    @RequestParam(required = false) String filter
    ) {
        return executorService.getAllExecutor(page, perPage, sort, order, filter);
    }
}

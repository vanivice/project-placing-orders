package com.attestation.project.app.controllers;

import com.attestation.project.app.model.dto.response.OrderCatalogResponse;
import com.attestation.project.app.model.dto.response.OrderInfoResponse;
import com.attestation.project.app.service.ExecutorService;
import com.attestation.project.app.service.OrderCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-catalog")
@RequiredArgsConstructor
@Tag(name = "каталог заказов")

public class OrderCatalogController {
    private final OrderCatalogService orderCatalogService;
    private final ExecutorService executorService;

    @GetMapping("/all")
    @Operation(summary = "Список заказов из каталога")
    public Page<OrderCatalogResponse> getAllCatalog(@RequestParam(defaultValue = "1") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer perPage,
                                                             @RequestParam(defaultValue = "id") String sort,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                             @RequestParam(required = false) String filter
    ) {
        return orderCatalogService.getAllCatalog(page, perPage, sort, order, filter);
    }

    @GetMapping("/get-order/{id}")
    @PreAuthorize("hasRole('EXECUTOR')")
    @Operation(summary = "Получить информацию о заказе по id в каталоге")
    public OrderInfoResponse getOrder(@PathVariable Long id) {
        return orderCatalogService.getCustomerOrder(id);
    }

    @GetMapping("/work/{id}")
    @PreAuthorize("hasRole('EXECUTOR')")
    @Operation(summary = "Взять заказ из каталога в работу по id в каталоге")
    public OrderCatalogResponse getOrderWork(@PathVariable Long id) {
        return orderCatalogService.getWorkOrder(id);
    }

    @PutMapping("/work/end/{id}")
    @PreAuthorize("hasRole('EXECUTOR')")
    @Operation(summary = "Завершить заказ по id в каталоге")
    public OrderCatalogResponse endOrderWork(@PathVariable Long id) {
        return orderCatalogService.finishWorkOrder(id);
    }

    @GetMapping("/get-my-work")
    @PreAuthorize("hasRole('EXECUTOR')")
    @Operation(summary = "Получить список заказов у себя в работе")
    public List<OrderCatalogResponse> getMyWorkOrder() {
        return executorService.getMyOrder();
    }
}

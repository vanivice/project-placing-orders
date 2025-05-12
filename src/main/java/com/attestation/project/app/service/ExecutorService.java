package com.attestation.project.app.service;

import com.attestation.project.app.exception.CommonBackendException;
import com.attestation.project.app.model.db.entity.Customer;
import com.attestation.project.app.model.db.entity.Executor;
import com.attestation.project.app.model.db.repository.ExecutorRepository;
import com.attestation.project.app.model.db.repository.OrderCatalogRepository;
import com.attestation.project.app.model.dto.request.ExecutorRequest;
import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.model.dto.response.ExecutorResponse;
import com.attestation.project.app.model.dto.response.OrderCatalogResponse;
import com.attestation.project.app.model.enums.Role;
import com.attestation.project.app.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecutorService {
    private final CustomerService customerService;
    private final ExecutorRepository executorRepository;
    private final ObjectMapper mapper;
    private final OrderCatalogRepository orderCatalogRepository;

    public ExecutorResponse updateExecutor(ExecutorRequest request) {
        Executor executorFromDB = executorRepository.findByEmail(customerService.getCurrentUser().getEmail());
        Executor executorRequest = mapper.convertValue(request, Executor.class);

        executorFromDB.setDescription(executorRequest.getDescription() == null ? executorFromDB.getDescription()
                : executorRequest.getDescription());

        executorFromDB = executorRepository.save(executorFromDB);
        return mapper.convertValue(executorFromDB, ExecutorResponse.class);
    }

    public CustomerResponse getRoleCustomer() {

        Customer customer = customerService.getCurrentUser();
        Executor executorFromDB = executorRepository.findByEmail(customer.getEmail());

        if (orderCatalogRepository.getMyWorkOrder(executorFromDB.getId()).size() != 0) {
            throw new CommonBackendException("Ошибка: у вас в работе есть заказы", HttpStatus.BAD_REQUEST);
        }

        customer.setRole(Role.ROLE_CUSTOMER);
        customerService.saveCus(customer);

        executorRepository.delete(executorFromDB);

        return CustomerResponse.builder()
                .message(customer.getUsername() + " теперь вы не можете выполнять заказы")
                .build();
    }

    public Page<ExecutorResponse> getAllExecutor(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtils.getPageRequest(page, perPage, sort, order);

        Page<Executor> executors;

        if (StringUtils.hasText(filter)) {
            executors = executorRepository.findAllFiltered(pageRequest, filter);
        } else {
            executors = executorRepository.findAll(pageRequest);
        }

        List<ExecutorResponse> content = executors.getContent().stream()
                .map(e -> mapper.convertValue(e, ExecutorResponse.class))
                .toList();

        return new PageImpl<>(content, pageRequest, executors.getTotalElements());
    }

    public List<OrderCatalogResponse> getMyOrder() {

        String currentEmail = customerService.getCurrentUser().getEmail();
        Long currentExeId = executorRepository.findByEmail(currentEmail).getId();

        if (orderCatalogRepository.getMyWorkOrder(currentExeId).isEmpty()) {
            throw new CommonBackendException("Ошибка: у вас нет заказов в работе", HttpStatus.NOT_FOUND);
        }

        return orderCatalogRepository.getMyWorkOrder(currentExeId).stream()
                .map(catalog -> mapper.convertValue(catalog, OrderCatalogResponse.class))
                .toList();
    }
}

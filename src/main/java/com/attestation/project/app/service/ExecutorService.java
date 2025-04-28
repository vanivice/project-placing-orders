package com.attestation.project.app.service;

import com.attestation.project.app.model.db.entity.Executor;
import com.attestation.project.app.model.db.repository.ExecutorRepository;
import com.attestation.project.app.model.dto.request.ExecutorRequest;
import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.model.dto.response.ExecutorResponse;
import com.attestation.project.app.model.enums.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutorService {
    private final CustomerService customerService;
    private final ExecutorRepository executorRepository;
    private final ObjectMapper mapper;

    // обновить информацию о себе
    public ExecutorResponse updateExecutor(ExecutorRequest request) {
        Executor executorFromDB = executorRepository.findByEmail(customerService.getCurrentUser().getEmail());
        Executor executorRequest = mapper.convertValue(request, Executor.class);

        executorFromDB.setDescription(executorRequest.getDescription() == null ? executorFromDB.getDescription()
                : executorRequest.getDescription());

        executorFromDB = executorRepository.save(executorFromDB);
        return mapper.convertValue(executorFromDB, ExecutorResponse.class);
    }

    // отказаться от роли исполнителя
    public CustomerResponse getCustomer() {
        var customer = customerService.getCurrentUser();
        customer.setRole(Role.ROLE_CUSTOMER);
        customerService.saveCus(customer);

        Executor executorFromDB = executorRepository.findByEmail(customer.getEmail());
        executorRepository.delete(executorFromDB);

        return CustomerResponse.builder()
                .message(customer.getUsername() + " теперь вы не можете выполнять заказы")
                .build();
    }
}

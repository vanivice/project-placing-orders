package com.attestation.project.app.service;

import com.attestation.project.app.exception.CommonBackendException;
import com.attestation.project.app.model.db.entity.Customer;
import com.attestation.project.app.model.db.entity.Executor;
import com.attestation.project.app.model.db.repository.CustomerRepository;
import com.attestation.project.app.model.db.repository.ExecutorRepository;
import com.attestation.project.app.model.dto.request.CustomerRequest;
import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.model.enums.Role;
import com.attestation.project.app.model.enums.UserStat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ExecutorRepository executorRepository;
    private final ObjectMapper mapper;

    // метод сохр. пользователя
    public void saveCus(Customer customer) {
        customerRepository.save(customer);
    }
    // метод сохр. исполнителя
    public void saveExe(Executor executor) {
        executorRepository.save(executor);
    }

    // метод созд. пользователя
    public void create (Customer customer) {

        if (!EmailValidator.getInstance().isValid(customer.getEmail())) {
            throw new CommonBackendException("Ошибка: неккоректный e-mail", HttpStatus.BAD_REQUEST);
        }

        if (customerRepository.existsByUsername(customer.getUsername())) {
            throw new CommonBackendException("Ошибка: логин занят", HttpStatus.BAD_REQUEST);
        }

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CommonBackendException("Ошибка: e-mail занят", HttpStatus.BAD_REQUEST);
        }

        saveCus(customer);
    }

    // метод возращает пользователя
//    public Customer getByUsername(String username) {
//        return customerRepository.findByUsername(username)
//                .orElseThrow(() -> new CommonBackendException("Ошибка: пользователь с таким логином не найден",
//                        HttpStatus.NOT_FOUND));
//    }

    public Customer getByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) {
            throw new CommonBackendException("Ошибка: пользователь с таким логином не найден", HttpStatus.NOT_FOUND);
        }
        return customer;
    }

    public Executor getByExeName(String email) {
        return executorRepository.findByEmail(email);
    }

    // метод удаляет пользователя
    public CustomerResponse deleteUser() {
        Customer getCurrentUser = getCurrentUser();

        if (getCurrentUser.getUsername() == null) {
            throw new CommonBackendException("Ошибка: пользователь с таким логином не найден", HttpStatus.BAD_REQUEST);
        }
        getCurrentUser.setStatus(UserStat.DELETED);
        customerRepository.save(getCurrentUser);

        return CustomerResponse.builder()
                .message("Профиль " + getCurrentUser.getUsername() + " удален")
                .build();
    }

    public CustomerResponse updateCustomer(CustomerRequest req) {
        Customer getCurrentUser = getCurrentUser();
        Executor getByExeName = getByExeName(getCurrentUser.getEmail());

        if (getCurrentUser.getUsername() == null) {
            throw new CommonBackendException("Ошибка: пользователь с таким логином не найден", HttpStatus.BAD_REQUEST);
        }

        Customer customerReq = mapper.convertValue(req, Customer.class);

        getCurrentUser.setEmail(customerReq.getEmail() == null ?
                getCurrentUser.getEmail() :
                customerReq.getEmail());
        getCurrentUser.setUsername(customerReq.getUsername() == null ?
                getCurrentUser.getUsername() :
                customerReq.getUsername());
        getCurrentUser.setFirstname(customerReq.getFirstname() == null ?
                getCurrentUser.getFirstname() :
                customerReq.getFirstname());

        if (getByExeName != null) {
            getByExeName.setEmail(getCurrentUser.getEmail());
            getByExeName.setFirstname(getCurrentUser.getFirstname());

            saveExe(getByExeName);
            saveCus(getCurrentUser);

            return mapper.convertValue(getCurrentUser, CustomerResponse.class);
        } else {
            saveCus(getCurrentUser);
        }

        CustomerResponse customer = mapper.convertValue(getCurrentUser, CustomerResponse.class);

        customer.setMessage("Данные профиля обновлены");

        return customer;

//        return CustomerResponse.builder()
//                .message(getCurrentUser.getUsername() + " Данные профиля обновлены")
////                .customer(customer)
//                .build();
    }


    // метод возращает пользователя (spring security)
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    // метод получения текущего пользователя
    public Customer getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    // выдача прав администратора
//    @Deprecated
//    public void getAdmin() {
//        var user = getCurrentUser();
//        user.setRole(Role.ADMINISTRATOR);
//        saveCus(user);
//    }

    // стать исполнителем
    public CustomerResponse getExecutor() {
        var customer = getCurrentUser();
        customer.setRole(Role.ROLE_EXECUTOR);
        saveCus(customer);

        Executor executor = Executor.builder()
                .firstname(customer.getFirstname())
                .email(customer.getEmail())
                .description("Информация о себе")
                .build();

        executor.setCustomer(customer);
        saveExe(executor);

        return CustomerResponse.builder()
                .message(customer.getUsername() + " теперь вы являетесь исполнителем")
                .build();
    }
}

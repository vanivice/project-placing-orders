package com.attestation.project.app.service;

import com.attestation.project.app.exception.CommonBackendException;
import com.attestation.project.app.model.db.entity.Customer;
import com.attestation.project.app.model.db.entity.Executor;
import com.attestation.project.app.model.db.entity.Order;
import com.attestation.project.app.model.db.entity.OrderCatalog;
import com.attestation.project.app.model.db.repository.ExecutorRepository;
import com.attestation.project.app.model.db.repository.OrderCatalogRepository;
import com.attestation.project.app.model.db.repository.OrderRepository;
import com.attestation.project.app.model.dto.response.OrderCatalogResponse;
import com.attestation.project.app.model.dto.response.OrderInfoResponse;
import com.attestation.project.app.model.enums.OrderStat;
import com.attestation.project.app.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor

public class OrderCatalogService {

    private final ObjectMapper mapper;
    private final OrderCatalogRepository orderCatalogRepository;
    private final OrderRepository orderRepository;
    private final ExecutorRepository executorRepository;
    private final OrderService orderService;
    private final CustomerService customerService;

    // получить информацию о заказе из каталога
    public OrderInfoResponse getCustomerOrder(Long id) {

        if (orderCatalogRepository.findById(id).isEmpty()) {
            throw new CommonBackendException("Ошибка: такого заказа нет в каталоге", HttpStatus.BAD_REQUEST);
        }

        Order order = orderRepository.getOrder(id);
        OrderInfoResponse orderResponse = mapper.convertValue(order, OrderInfoResponse.class);
        Customer customerName = orderRepository.findByCatalogId(id).getCustomer();

        orderResponse.setCustomerName(customerName.getUsername());
        orderResponse.setCustomerId(customerName.getId());

        orderResponse.setMessage("Для связи с "
                + customerName.getUsername()
                + " используйте сообщения /message/send/"
                + customerName.getId());

        return orderResponse;
    }

    // просмотерть весь каталог заказов
    public Page<OrderCatalogResponse> getAllCatalog(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtils.getPageRequest(page, perPage, sort, order);

        Page<OrderCatalog> orderCatalogs;

        if (StringUtils.hasText(filter)) {
            orderCatalogs = orderCatalogRepository.findAllFiltered(pageRequest, filter);
        } else {
            orderCatalogs = orderCatalogRepository.findAll(pageRequest);
        }

        List<OrderCatalogResponse> content = orderCatalogs.getContent().stream()
                .map(o -> mapper.convertValue(o, OrderCatalogResponse.class))
                .toList();

        return new PageImpl<>(content, pageRequest, orderCatalogs.getTotalElements());
    }

    // взять заказ в работу
    public OrderCatalogResponse getWorkOrder(Long id) {

        if (orderCatalogRepository.findById(id).isEmpty()) {
            throw new CommonBackendException("Ошибка: такого заказа нет в каталоге", HttpStatus.BAD_REQUEST);
        }

        if (orderService.getCatalogFromDB(id).getStatus() == OrderStat.AT_WORK) {
            throw new CommonBackendException("Ошибка: этот заказ уже в работе", HttpStatus.BAD_REQUEST);
        }

        String email = customerService.getCurrentUser().getEmail();
        Executor executorFromDB = executorRepository.findByEmail(email);
        OrderCatalog catalogFromDB = orderService.getCatalogFromDB(id);

        List<OrderCatalog> catalogList = executorFromDB.getOrderCatalogs();
        Order orderFromDBByCatalog = orderRepository.findByCatalogId(id);

        orderFromDBByCatalog.setStatus(OrderStat.AT_WORK);

        catalogList.add(catalogFromDB);
        catalogFromDB.setExecutor(executorFromDB);
        catalogFromDB.setStatus(OrderStat.AT_WORK);

        orderRepository.save(orderFromDBByCatalog);
        orderCatalogRepository.save(catalogFromDB);
        OrderCatalogResponse message = new OrderCatalogResponse();
        message.setMessage("Заказ "
                + orderFromDBByCatalog.getId()
                + " успешно взят в работу");

        return message;
    }

    // завершить заказ
    public OrderCatalogResponse finishWorkOrder(Long catalogId) {

        String email = customerService.getCurrentUser().getEmail();
        Executor executorFromDB = executorRepository.findByEmail(email);

        if (orderCatalogRepository.findById(catalogId).isEmpty()) {
            throw new CommonBackendException("Ошибка: такого заказа нет в каталоге", HttpStatus.BAD_REQUEST);
        }

        if (orderService.getCatalogFromDB(catalogId).getStatus() == OrderStat.COMPLETED) {
            throw new CommonBackendException("Ошибка: этот заказ уже завершен", HttpStatus.BAD_REQUEST);
        }

        if (orderService.getCatalogFromDB(catalogId).getExecutor() == null) {
            throw new CommonBackendException("Ошибка: над этим заказом никто не работает", HttpStatus.BAD_REQUEST);
        }

        if (!orderService.getCatalogFromDB(catalogId).getExecutor().getId().equals(executorFromDB.getId())) {
            throw new CommonBackendException("Ошибка: вы не работаете над этим заказом", HttpStatus.BAD_REQUEST);
        }

        OrderCatalog catalogFromDB = orderService.getCatalogFromDB(catalogId);
        catalogFromDB.setStatus(OrderStat.COMPLETED);

        Order orderFromDBByCatalog = orderRepository.findByCatalogId(catalogId);
        orderFromDBByCatalog.setStatus(OrderStat.COMPLETED);

        orderRepository.save(orderFromDBByCatalog);
        orderCatalogRepository.save(catalogFromDB);
        OrderCatalogResponse message = new OrderCatalogResponse();
        message.setMessage("Заказ "
                + orderFromDBByCatalog.getId()
                + " завершен");

        return message;
    }
}

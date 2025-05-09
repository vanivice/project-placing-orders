package com.attestation.project.app.service;

import com.attestation.project.app.exception.CommonBackendException;
import com.attestation.project.app.model.db.entity.Order;
import com.attestation.project.app.model.db.entity.OrderCatalog;
import com.attestation.project.app.model.db.repository.OrderCatalogRepository;
import com.attestation.project.app.model.db.repository.OrderRepository;
import com.attestation.project.app.model.dto.request.OrderInfoRequest;
import com.attestation.project.app.model.dto.response.OrderInfoResponse;
import com.attestation.project.app.model.enums.OrderStat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ObjectMapper mapper;
    private final OrderRepository orderRepository;
    private final OrderCatalogRepository orderCatalogRepository;
    private final CustomerService customerService;

    // создать заказ
    public OrderInfoResponse createOrder(OrderInfoRequest request) {

        orderRepository.findByTitleAndCost(request.getTitle(), request.getCost())
                .ifPresent(order -> {
                    throw new CommonBackendException("Ошибка: такой заказ уже создан", HttpStatus.CONFLICT);
                });

        Order order = mapper.convertValue(request, Order.class);

        OrderCatalog orderCatalog = OrderCatalog.builder()
                .description(order.getTitle())
                .status(OrderStat.CREATED)
                .build();

        order.setCreateDate(LocalDateTime.now());
        order.setCustomer(customerService.getCurrentUser());
        order.setCatalog(orderCatalog);
        order.setStatus(OrderStat.CREATED);

        Order save = orderRepository.save(order);

        return mapper.convertValue(save, OrderInfoResponse.class);
    }

    // обновить данные о заказе
    public OrderInfoResponse updateMyOrder(String numberOrder, OrderInfoRequest req) {

        Order orderFromDB = getOrderFromDB(numberOrder);

        if (!Objects.equals(orderFromDB.getCustomer().getId(), customerService.getCurrentUser().getId())) {
            throw new CommonBackendException("Ошибка: можно редактировать только свои заказы", HttpStatus.CONFLICT);
        }

        if (orderFromDB.getStatus() == OrderStat.AT_WORK) {
            throw new CommonBackendException("Ошибка: нельзя редактировать заказ, который находится в работе," +
                    "свяжитесь с исполнителем", HttpStatus.CONFLICT);
        }

        Order orderReq = mapper.convertValue(req, Order.class);

        orderFromDB.setTitle(orderReq.getTitle() == null ? orderFromDB.getTitle() : orderReq.getTitle());
        orderFromDB.setRequirements(orderReq.getRequirements() == null ? orderFromDB.getRequirements()
                : orderReq.getRequirements());
        orderFromDB.setDeadlines(orderReq.getDeadlines() == null ? orderFromDB.getDeadlines()
                : orderReq.getDeadlines());
        orderFromDB.setCost(orderReq.getCost() == null ? orderFromDB.getCost() : orderReq.getCost());
        orderFromDB.setStatus(OrderStat.UPDATED);

        OrderCatalog catalogFromDB = getCatalogFromDB(orderFromDB.getCatalog().getId());

        orderFromDB.setCatalog(catalogFromDB);
        catalogFromDB.setStatus(OrderStat.UPDATED);
        catalogFromDB.setDescription(orderFromDB.getTitle());

        orderCatalogRepository.save(catalogFromDB);
        orderFromDB = orderRepository.save(orderFromDB);

        OrderInfoResponse orderInfoResponse = mapper.convertValue(orderFromDB, OrderInfoResponse.class);
        orderInfoResponse.setMessage("Данные по заказу успешно обновлены");

        return orderInfoResponse;
    }

    // поиск заказа по номеру заказа
    public Order getOrderFromDB(String number) {
        Optional<Order> optionalOrder = orderRepository.findById(number);
        final String errMsg = ("Заказ: " + number + " не найден");
        return optionalOrder.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    // поиск заказа в каталоге по номеру каталога
    public OrderCatalog getCatalogFromDB(Long id) {
        Optional<OrderCatalog> optionalCatalog = orderCatalogRepository.findById(id);
        final String errMsg = ("Заказ с номером в каталоге: " + id + " не найден");
        return optionalCatalog.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    // получить заказ по номеру
    public OrderInfoResponse getCustomerOrder(String number) {
        Order order = getOrderFromDB(number);

        OrderInfoResponse orderInfoResponse = mapper.convertValue(order, OrderInfoResponse.class);
        orderInfoResponse.setCustomerName(order.getCustomer().getUsername());

        return orderInfoResponse;
    }

    // получить свои заказы
    public List<OrderInfoResponse> getMyOrder() {

        Long currentId = customerService.getCurrentUser().getId();

        if (orderRepository.getMyOrder(currentId).isEmpty()) {
            throw new CommonBackendException("Ошибка: вы не сделали ни одного заказа", HttpStatus.NOT_FOUND);
        }

        return orderRepository.getMyOrder(currentId).stream()
                .map(order -> mapper.convertValue(order, OrderInfoResponse.class))
                .toList();
    }

    // удалить заказ
    public OrderInfoResponse deleteMyOrder (String number){
        Order orderFromDB = getOrderFromDB(number);

        if (!Objects.equals(orderFromDB.getCustomer().getId(), customerService.getCurrentUser().getId())) {
            throw new CommonBackendException("Ошибка: вы не можете удалить этот заказ", HttpStatus.CONFLICT);
        }

        if(orderFromDB.getStatus() == OrderStat.AT_WORK) {
            throw new CommonBackendException("Ошибка: нельзя удалить заказ, который находится в работе," +
                    "свяжитесь с исполнителем", HttpStatus.CONFLICT);
        }

        OrderCatalog catalogFromDB = getCatalogFromDB(orderFromDB.getCatalog().getId());

        orderRepository.delete(orderFromDB);
        orderCatalogRepository.delete(catalogFromDB);

        OrderInfoResponse message = new OrderInfoResponse();
        message.setMessage("Заказ: " + orderFromDB.getId() + "удален");

        return message;
    }
}

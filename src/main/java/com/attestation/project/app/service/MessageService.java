package com.attestation.project.app.service;

import com.attestation.project.app.exception.CommonBackendException;
import com.attestation.project.app.model.db.entity.Customer;
import com.attestation.project.app.model.db.entity.Message;
import com.attestation.project.app.model.db.repository.CustomerRepository;
import com.attestation.project.app.model.db.repository.MessageRepository;
import com.attestation.project.app.model.dto.request.MessageRequest;
import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.model.dto.response.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class MessageService {

    private final MessageRepository messageRepository;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final ObjectMapper mapper;

    public Message getMessageFromDB(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        final String errMsg = ("Ошибка: такого сообщения нет");
        return optionalMessage.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    public List<Message> getMessagesByUser() {
        Customer customer = customerService.getCurrentUser();
        return messageRepository.findBySenderOrReceiver(customer, customer);
    }

    public List<Message> getMessagesBetween(CustomerResponse user1, CustomerResponse user2) {
        return messageRepository.findMessagesBetween(user1.getId(), user2.getId());
    }

    // метод для просмотра сообщений с конкретным пользователем
    public List<MessageResponse> getMessagePartners(Long receiverId) {

        Customer currentCustomer = customerService.getCurrentUser();
        Customer partnerCustomer = customerRepository.findById(receiverId).orElse(null);

        CustomerResponse current = mapper.convertValue(currentCustomer, CustomerResponse.class);
        CustomerResponse partner = mapper.convertValue(partnerCustomer, CustomerResponse.class);


        if (partnerCustomer == null) {
            throw new CommonBackendException("Ошибка: пользователь с таким id не найден", HttpStatus.NOT_FOUND);
        }

        List<Message> messages = getMessagesBetween(current, partner);

        return messages.stream()
                .map(message -> mapper.convertValue(message, MessageResponse.class))
                .collect(Collectors.toList());
    }

    // метод для отправки сообщений
    public MessageResponse sendMessage(Long receiverId, MessageRequest request) {
        Customer sender = customerService.getCurrentUser();
        Customer receiver = customerRepository.findById(receiverId).orElseThrow();

        Message message = mapper.convertValue(request, Message.class);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setTimestamp(Date.from(LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toInstant()));

        messageRepository.save(message);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Сообщение "
                + "'"
                + message.getContent()
                + "'"
                + " отправлено "
                + receiver.getUsername());

        return mapper.convertValue(messageResponse, MessageResponse.class);
    }

    // удалить сообщение
    public MessageResponse deleteMessage(Long id) {

        Message messageFromDB = getMessageFromDB(id);
        if (!messageFromDB.getSender().getId().equals(customerService.getCurrentUser().getId()) &&
                !messageFromDB.getReceiver().getId().equals(customerService.getCurrentUser().getId())) {
            throw new CommonBackendException("Ошибка: такого сообщения нет", HttpStatus.CONFLICT);
        }

        messageRepository.delete(messageFromDB);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Сообщение " + id + " удалено");

        return messageResponse;
    }
}

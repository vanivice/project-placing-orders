package com.attestation.project.app.controllers;

import com.attestation.project.app.model.dto.request.MessageRequest;
import com.attestation.project.app.model.dto.response.MessageResponse;
import com.attestation.project.app.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Tag(name = "сообщения")

public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send/{id}")
    @Operation(summary = "Отправить сообщение пользователю по id пользователя")
    public MessageResponse sendMessage(@PathVariable Long id, @RequestBody MessageRequest req) {
        return messageService.sendMessage(id, req);
    }

    @GetMapping("/my-message")
    @Operation(summary = "Посмотреть свои сообщения")
    public List<MessageResponse> getMessagesByUser() {
        return messageService.getMessagesByUser();
    }

    @GetMapping("/chat/{id}")
    @Operation(summary = "Посмотреть чат с пользователем по его id")
    public List<MessageResponse> getChatPartners(@PathVariable Long id) {
        return messageService.getMessagePartners(id);
    }

    @DeleteMapping("/chat/delete/{id}")
    @Operation(summary = "Удалить сообщение по id")
    public MessageResponse deleteMessage(@PathVariable Long id) {
        return messageService.deleteMessage(id);
    }
}

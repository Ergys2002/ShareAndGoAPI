package com.app.ShareAndGo.controllers;


import com.app.ShareAndGo.dto.requests.MessageRequest;
import com.app.ShareAndGo.dto.responses.MessageResponse;
import com.app.ShareAndGo.entities.Message;
import com.app.ShareAndGo.services.interfaces.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final IMessageService messageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload MessageRequest messageRequest){
        MessageResponse savedMsg = messageService.saveMessage(messageRequest);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(messageRequest.getRecipientId()),
                "queue/messages",
                savedMsg
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<MessageResponse>> findChatMessages(@PathVariable String senderId,
                                                                  @PathVariable String recipientId) {
        return ResponseEntity
                .ok(messageService.findChatMessages(senderId, recipientId));
    }
}

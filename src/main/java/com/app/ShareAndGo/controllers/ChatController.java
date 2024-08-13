//package com.app.ShareAndGo.controllers;
//
//import com.app.ShareAndGo.entities.Message;
//import com.app.ShareAndGo.repositories.MessageRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@Controller
//@RequiredArgsConstructor
//public class ChatController {
//
//    private SimpMessagingTemplate template;
//
//    private MessageRepository messageRepository;
//
//    /**
//     * Sends a message to its destination channel
//     *
//     * @param message
//     */
//    @MessageMapping("/messages")
//    public void handleMessage(Message message) {
//        message.setTimestamp(LocalDateTime.now());
//        messageRepository.save(message);
//        template.convertAndSend("/channel/chat/" + message.getChannel(), message);
//    }
//}
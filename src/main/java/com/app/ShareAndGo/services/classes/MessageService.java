package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.MessageRequest;
import com.app.ShareAndGo.dto.responses.MessageResponse;
import com.app.ShareAndGo.entities.Chat;
import com.app.ShareAndGo.entities.Message;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.repositories.ChatRepository;
import com.app.ShareAndGo.repositories.MessageRepository;
import com.app.ShareAndGo.services.interfaces.IChatService;
import com.app.ShareAndGo.services.interfaces.IMessageService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    private final IUserService userService;
    private final IChatService chatService;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    @Override
    public MessageResponse saveMessage(MessageRequest messageRequest) {
        User sender = userService.getUserById(messageRequest.getSenderId());
        User recipient = userService.getUserById(messageRequest.getRecipientId());

        String chatIdentifier = chatService.getChatId(sender, recipient, true);
        if (chatIdentifier == null){
            throw new RuntimeException("Error creating chat");
        }
        Chat chat = chatRepository.findByChatIdentifier(chatIdentifier);
        if (chat == null){
            throw new RuntimeException("Chat not found");
        }
        Message message = Message.builder()
                .sender(sender)
                .receiver(recipient)
                .content(messageRequest.getContent())
                .chat(chat)
                .build();

        Message savedMsg = messageRepository.save(message);

        return MessageResponse.builder()
                .id(savedMsg.getId())
                .recipientId(recipient.getId())
                .senderId(sender.getId())
                .content(savedMsg.getContent())
                .build();
    }

    @Override
    public List<MessageResponse> findChatMessages(String senderId, String recipientId) {
        User sender = userService.getUserById(Long.parseLong(senderId));
        User recipient = userService.getUserById(Long.parseLong(recipientId));
        String chatIdentifier = chatService.getChatId(sender, recipient, false);

        Chat chat = chatRepository.findByChatIdentifier(chatIdentifier);

        return chat.getMessages().stream().map(message -> MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .recipientId(message.getReceiver().getId())
                .content(message.getContent())
                .build()).toList();
    }
}

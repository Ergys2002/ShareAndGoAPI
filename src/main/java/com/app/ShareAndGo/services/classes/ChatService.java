package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.entities.Chat;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.repositories.ChatRepository;
import com.app.ShareAndGo.services.interfaces.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {
    private final ChatRepository chatRepository;
    @Override
    public String getChatId(User sender, User recipient, boolean createChatIfNotExists){
        Chat chat = chatRepository.findChatBySenderAndRecipient(sender, recipient);

        if (chat == null){
            if (createChatIfNotExists){
                return createChatId(sender, recipient);
            }
            return null;
        }

        return chat.getChatIdentifier();
    }

    private String createChatId(User sender, User recipient) {
        String chatIdentifier = sender.getId() + "_" + recipient.getId();

        Chat senderRecipientChat = Chat.builder()
                .chatIdentifier(chatIdentifier)
                .sender(sender)
                .recipient(recipient)
                .build();

        Chat recipientSenderChat = Chat.builder()
                .chatIdentifier(chatIdentifier)
                .sender(recipient)
                .recipient(sender)
                .build();

        chatRepository.save(recipientSenderChat);
        chatRepository.save(senderRecipientChat);

        return chatIdentifier;
    }
}

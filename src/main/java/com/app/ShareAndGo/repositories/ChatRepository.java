package com.app.ShareAndGo.repositories;


import com.app.ShareAndGo.entities.Chat;
import com.app.ShareAndGo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findChatBySenderAndRecipient(User sender, User recipient);

    Chat findByChatIdentifier(String chatIdentifier);
}

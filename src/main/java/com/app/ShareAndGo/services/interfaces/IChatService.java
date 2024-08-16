package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.entities.User;

public interface IChatService {
    String getChatId(User sender, User recipient, boolean createChatIfNotExists);
}

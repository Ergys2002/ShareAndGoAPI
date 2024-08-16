package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.MessageRequest;
import com.app.ShareAndGo.dto.responses.MessageResponse;
import com.app.ShareAndGo.entities.Message;

import java.util.List;

public interface IMessageService {
    MessageResponse saveMessage(MessageRequest messageRequest);

    List<MessageResponse> findChatMessages(String senderId, String recipientId);


}

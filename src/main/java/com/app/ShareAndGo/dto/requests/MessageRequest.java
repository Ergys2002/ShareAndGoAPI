package com.app.ShareAndGo.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MessageRequest {
    private String chatId;
    private Long senderId;
    private Long recipientId;
    private String content;
}

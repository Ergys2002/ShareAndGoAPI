package com.app.ShareAndGo.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MessageResponse {
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String content;
}

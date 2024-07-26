package com.app.ShareAndGo.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class AdminLoginResponse {
    private String token;
    private String message;
    private LocalDateTime expiration;
    private String role;
}

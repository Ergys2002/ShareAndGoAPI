package com.app.ShareAndGo.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserLoginRequest {
    private String email;
    private String password;
}

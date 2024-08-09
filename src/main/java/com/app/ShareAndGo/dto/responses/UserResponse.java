package com.app.ShareAndGo.dto.responses;

import com.app.ShareAndGo.entities.UserProfile;

public interface UserResponse {
    String getPhoneNumber();
    double getAccountBalance();
    String getEmail();
    UserProfile getProfile();
}

package com.app.ShareAndGo.dto.responses;

import com.app.ShareAndGo.entities.UserProfile;
import com.app.ShareAndGo.enums.Role;

import java.time.LocalDate;

public interface AdminResponse {
    String getPhoneNumber();
    double getAccountBalance();
    String getEmail();
    String getNid();
    double getSalary();
    Role getRole();

    //Date of hiring might be shown at profile of admin
    LocalDate getCreatedAt();
    UserProfile getProfile();
}

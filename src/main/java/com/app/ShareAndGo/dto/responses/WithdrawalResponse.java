package com.app.ShareAndGo.dto.responses;

import com.app.ShareAndGo.enums.WithdrawalStatus;
import org.springframework.beans.factory.annotation.Value;

public interface WithdrawalResponse {
    Long getId();
    WithdrawalStatus getWithdrawalStatus();
    String getAccountNumber();
    double getAmount();
    @Value("#{target.user.id}")
    Long getUserId();

    @Value("#{target.user.profile.firstname}")
    String getUserFirstname();

    @Value("#{target.user.profile.lastname}")
    String getUserLastname();

    @Value("#{target.user.profile.profilePictureUrl}")
    String getUserProfilePictureURL();
}

package com.app.ShareAndGo.dto.requests;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class WithdrawalRequest {
    private String accountNumber;

    private double amount;
}

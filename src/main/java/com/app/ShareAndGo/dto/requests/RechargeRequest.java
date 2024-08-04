package com.app.ShareAndGo.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RechargeRequest {
    private double amount;
    private String cardNumber;
    private String cvv;
    private String expirationDate;
    private String nameOnCard;
}

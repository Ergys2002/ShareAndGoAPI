package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.RechargeRequest;
import org.springframework.http.ResponseEntity;

public interface IRechargeService {
    ResponseEntity<?> recharge(RechargeRequest rechargeRequest);
}

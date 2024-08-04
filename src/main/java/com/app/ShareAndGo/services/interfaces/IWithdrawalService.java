package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.WithdrawalRequest;
import org.springframework.http.ResponseEntity;

public interface IWithdrawalService {

    ResponseEntity<?> requestWithdrawal(WithdrawalRequest withdrawalRequest);

    ResponseEntity<?> getPendingWithdrawals();

    ResponseEntity<?> confirmWithdrawal(Long id);

    ResponseEntity<?> rejectWithdrawal(Long id);
}

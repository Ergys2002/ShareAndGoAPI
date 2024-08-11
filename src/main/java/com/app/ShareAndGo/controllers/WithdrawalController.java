package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.WithdrawalRequest;
import com.app.ShareAndGo.services.interfaces.IWithdrawalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/withdrawal")
@AllArgsConstructor
public class WithdrawalController {

    private final IWithdrawalService withdrawalService;

    @PostMapping("/withdraw")
    public ResponseEntity<?> requestWithdrawal(@RequestBody  WithdrawalRequest withdrawalRequest){
        return withdrawalService.requestWithdrawal(withdrawalRequest);
    }

    @GetMapping("/admin/pending-withdrawals")
    public ResponseEntity<?> getPendingWithdrawals(){
        return withdrawalService.getPendingWithdrawals();
    }

    @PutMapping("/admin/confirm-withdrawal")
    public ResponseEntity<?> confirmWithdrawal(@RequestParam("id") Long id){
        return withdrawalService.confirmWithdrawal(id);
    }

    @PutMapping("/admin/reject-withdrawal")
    public ResponseEntity<?> rejectWithdrawal(@RequestParam("id") Long id){
        return withdrawalService.rejectWithdrawal(id);
    }

    @GetMapping("/all-withdrawals")
    public ResponseEntity<?> getAllWithdrawalsOfAuthenticatedUser(){
        return withdrawalService.getWithdrawalsOfAuthenticatedUser();
    }
}

package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.RechargeRequest;
import com.app.ShareAndGo.services.interfaces.IRechargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recharge")
@RequiredArgsConstructor
public class RechargeController {

    private final IRechargeService rechargeService;
    @PostMapping
    public ResponseEntity<?> rechargeBalance(@RequestBody RechargeRequest rechargeRequest){
        return rechargeService.recharge(rechargeRequest);
    }
}

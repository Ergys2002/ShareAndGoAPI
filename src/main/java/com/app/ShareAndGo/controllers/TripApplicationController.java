package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.TripApplicationRequest;
import com.app.ShareAndGo.services.interfaces.ITripApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trip-application")
@RequiredArgsConstructor
public class TripApplicationController{
    private final ITripApplicationService tripApplicationService;

    @PostMapping("/apply-to-reserve")
    public ResponseEntity<?> applyForTripReservation(TripApplicationRequest tripApplicationRequest){
        return tripApplicationService.applyForTripReservation(tripApplicationRequest);
    }
}

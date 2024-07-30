package com.app.ShareAndGo.controllers;


import com.app.ShareAndGo.dto.requests.TripCreationRequest;
import com.app.ShareAndGo.services.interfaces.ITripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final ITripService tripService;

    @PostMapping("/create-trip")
    public ResponseEntity<?> createTrip(@RequestBody TripCreationRequest tripData){
        return tripService.createTrip(tripData);
    }
}

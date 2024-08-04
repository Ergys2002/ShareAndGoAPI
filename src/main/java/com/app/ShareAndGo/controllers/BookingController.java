package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.services.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final IBookingService bookingService;

    @PutMapping("/confirm-application")
    public ResponseEntity<?> confirmTripApplication(@RequestParam("id") Long applicationId){
        return bookingService.confirmTripApplication(applicationId);
    }

    @PutMapping("/reject-application")
    public ResponseEntity<?> rejectTripApplication(@RequestParam("id") Long applicationId){
        return bookingService.rejectTripApplication(applicationId);
    }
}

package com.app.ShareAndGo.services.interfaces;

import org.springframework.http.ResponseEntity;

public interface IBookingService {
    ResponseEntity<?> confirmTripApplication(Long applicationId);

    ResponseEntity<?> rejectTripApplication(Long applicationId);

    ResponseEntity<?> getBookingsByTripId(Long tripId);
}

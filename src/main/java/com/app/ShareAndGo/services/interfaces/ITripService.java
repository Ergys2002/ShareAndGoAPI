package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.TripCreationRequest;
import com.app.ShareAndGo.entities.Trip;
import org.springframework.http.ResponseEntity;

public interface ITripService {
    ResponseEntity<?> createTrip(TripCreationRequest tripData);
    Trip getLatestTripOfAuthenticatedUser();
    ResponseEntity<?> getAllTrips();
}

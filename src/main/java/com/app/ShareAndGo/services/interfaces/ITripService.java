package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.TripCreationRequest;
import com.app.ShareAndGo.entities.Trip;
import org.springframework.http.ResponseEntity;

public interface ITripService {
    ResponseEntity<?> createTrip(TripCreationRequest tripData);
    Trip getLatestTripOfAuthenticatedUser();
    ResponseEntity<?> getAllTrips(int page, int size);

    ResponseEntity<?> get3LatestTrips();

    ResponseEntity<?> getFilteredTrips(int page, int size, String startCity, String endCity, String date);

    ResponseEntity<?> cancelTrip(Long tripId);

    ResponseEntity<?> payForTrip(Long id);

    Trip getById(Long id);

    ResponseEntity<?> getTripById(Long id);

    ResponseEntity<?> getTripsAsDriver();

    ResponseEntity<?> getTripsAsPassenger();

    ResponseEntity<?> endActiveTripOfAuthenticatedUser();

    ResponseEntity<?> getActiveTripsAsDriver();

    ResponseEntity<?> getActiveTripsAsPassenger();

    ResponseEntity<?> getPassengersByTripId(Long tripId);
}

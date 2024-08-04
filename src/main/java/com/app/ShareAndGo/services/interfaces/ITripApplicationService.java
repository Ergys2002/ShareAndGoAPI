package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.TripApplicationRequest;
import org.springframework.http.ResponseEntity;

public interface ITripApplicationService {
    ResponseEntity<?> applyForTripReservation(TripApplicationRequest tripApplicationRequest);
}

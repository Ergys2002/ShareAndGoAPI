package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.TripApplicationRequest;
import com.app.ShareAndGo.repositories.TripApplicationRepository;
import com.app.ShareAndGo.services.interfaces.ITripApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TripApplicationService  implements ITripApplicationService {
    private final TripApplicationRepository tripApplicationRepository;

    @Override
    public ResponseEntity<?> applyForTripReservation(TripApplicationRequest tripApplicationRequest) {
        return null;
    }
}

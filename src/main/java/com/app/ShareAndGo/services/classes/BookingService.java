package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.entities.TripApplication;
import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.repositories.BookingRepository;
import com.app.ShareAndGo.repositories.TripApplicationRepository;
import com.app.ShareAndGo.services.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final TripApplicationRepository tripApplicationRepository;

    @Override
    public ResponseEntity<?> confirmTripApplication(Long applicationId) {
        return null;
    }

    @Override
    public ResponseEntity<?> rejectTripApplication(Long applicationId) {
        TripApplication tripApplication = tripApplicationRepository.findById(applicationId).orElse(null);

        if (tripApplication == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aplikimi qe doni te anuloni nuk ekziston");
        }

        tripApplication.setStatus(ApplicationStatus.REJECTED);
        tripApplicationRepository.save(tripApplication);

        return ResponseEntity.status(HttpStatus.OK).body("Aplikimi u anulua");
    }
}

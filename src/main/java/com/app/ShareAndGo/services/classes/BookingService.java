package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.entities.Booking;
import com.app.ShareAndGo.entities.Package;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripApplication;
import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.enums.BookingType;
import com.app.ShareAndGo.repositories.BookingRepository;
import com.app.ShareAndGo.repositories.PackageRepository;
import com.app.ShareAndGo.repositories.TripApplicationRepository;
import com.app.ShareAndGo.repositories.TripRepository;
import com.app.ShareAndGo.services.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final TripApplicationRepository tripApplicationRepository;
    private final TripRepository tripRepository;
    private final PackageRepository packageRepository;

    @Override
    @Transactional
    public ResponseEntity<?> confirmTripApplication(Long applicationId) {
        TripApplication tripApplication = tripApplicationRepository.findById(applicationId).orElse(null);

        if (tripApplication == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aplikimi qe doni te konfirmoni nuk ekziston");
        }

        Trip trip = tripRepository.findById(tripApplication.getTrip().getId()).orElse(null);
        if (trip == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Udhetimi nuk ekziston");
        }

        tripApplication.setStatus(ApplicationStatus.ACCEPTED);
        tripApplicationRepository.save(tripApplication);
        trip.setAvailableSeats(trip.getAvailableSeats() - tripApplication.getNumberOfSeats());
        tripRepository.save(trip);

        Set<TripApplication> pendingApplicationsOfTrip = tripApplicationRepository.findTripApplicationsByTripAndStatus(trip, ApplicationStatus.PENDING);
        pendingApplicationsOfTrip.forEach(application -> {
            if (application.getNumberOfSeats() > trip.getAvailableSeats()){
                application.setStatus(ApplicationStatus.REJECTED);
            }
        });

        tripApplicationRepository.saveAll(pendingApplicationsOfTrip);

        Booking booking = Booking.builder()
                .bookingType(BookingType.valueOf(tripApplication.getApplicationType().toString()))
                .trip(trip)
                .passenger(tripApplication.getApplicant())
                .reservedSeats(tripApplication.getNumberOfSeats())
                .build();

        Set<Package> packages = tripApplication.getPackages();
        packages.forEach(aPackage -> aPackage.setBooking(booking));
        packageRepository.saveAll(packages);
        bookingRepository.save(booking);

        return ResponseEntity.status(HttpStatus.OK).body("Aplikimi u pranua");
    }

    @Override
    public ResponseEntity<?> rejectTripApplication(Long applicationId) {
        TripApplication tripApplication = tripApplicationRepository.findById(applicationId).orElse(null);

        if (tripApplication == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aplikimi qe doni te anuloni nuk ekziston");
        }

        tripApplication.setStatus(ApplicationStatus.REJECTED);
        tripApplicationRepository.save(tripApplication);

        Set<Package> packages = tripApplication.getPackages();
        packageRepository.deleteAll(packages);


        return ResponseEntity.status(HttpStatus.OK).body("Aplikimi u anulua");
    }
}

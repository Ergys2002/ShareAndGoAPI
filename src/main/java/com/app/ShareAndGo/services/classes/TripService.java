package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.TripCreationRequest;
import com.app.ShareAndGo.dto.responses.TripResponse;
import com.app.ShareAndGo.entities.*;
import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.enums.BookingStatus;
import com.app.ShareAndGo.enums.TripStatus;
import com.app.ShareAndGo.repositories.*;
import com.app.ShareAndGo.services.interfaces.ITransactionService;
import com.app.ShareAndGo.services.interfaces.ITripService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripService implements ITripService {

    private final IUserService userService;
    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ITransactionService transactionService;
    private final TripApplicationRepository tripApplicationRepository;
    private final CarRepository carRepository;

    @Override
    public ResponseEntity<?> createTrip(TripCreationRequest tripData) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Set<Trip> tripsOfAuthenticatedDriver = tripRepository.findAllByDriver(authenticatedUser);
        Car car = carRepository.findById(tripData.getCarId()).orElse(null);
        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Makina nuk ekziston");
        }

        Trip newTrip = Trip.builder()
                .startCity(tripData.getStartCity())
                .endCity(tripData.getEndCity())
                .totalSeats(tripData.getPassengerCount())
                .availableSeats(tripData.getPassengerCount())
                .date(LocalDate.parse(tripData.getDateOfTrip()))
                .distance(tripData.getDistance())
                .time(LocalTime.parse(tripData.getTimeOfTrip()))
                .pricePerSeat(tripData.getPricePerSeat())
                .duration(tripData.getDuration())
                .driver(authenticatedUser)
                .car(car)
                .build();

        boolean hasInterferingTrips = tripsOfAuthenticatedDriver.stream().anyMatch(trip -> checkOverlap(trip, newTrip));

        if (LocalDateTime.parse(tripData.getDateOfTrip() + "T" + tripData.getTimeOfTrip()).isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Koha e udhetimit tuaj nuk mund te jete ne te shkuaren");
        } else {
            if (hasInterferingTrips) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Udhetimi qe ju doni te postoni ka perplasje orari me nje nga udhetimet e tjera qe ju keni postuar");
            } else {
                tripRepository.save(newTrip);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Udhetimi juaj u postua me sukses");
            }
        }
    }

    private boolean checkOverlap(Trip existingTrip, Trip newTrip) {
        LocalTime startTimeOfExistingTrip = existingTrip.getTime();
        LocalTime startTimeOfNewTrip = newTrip.getTime();
        LocalTime endTimeOfExistingTrip = existingTrip.getTime().plusMinutes((long) existingTrip.getDuration() * 60);
        LocalTime endTimeOfNewTrip = newTrip.getTime().plusMinutes((long) newTrip.getDuration() * 60);

        if (!existingTrip.getDate().isEqual(newTrip.getDate())) {
            return false;
        } else {
            return startTimeOfNewTrip.isBefore(endTimeOfExistingTrip) && endTimeOfNewTrip.isAfter(startTimeOfExistingTrip);
        }
    }

    public Trip getLatestTripOfAuthenticatedUser() {
        return tripRepository.findFirstByDriverOrderByIdDesc(userService.getAuthenticatedUser());
    }

    public Trip getById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> getTripById(Long id) {
        TripResponse tripResponse = tripRepository.getTripById(id);
        if (tripResponse == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Udhetimi nuk ekziston");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tripResponse);
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    public ResponseEntity<?> getAllTrips(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<TripResponse> trips = tripRepository.getAll(pageable).stream().toList();

        if (trips == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk ekziston asnje udhetim aktiv");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(trips);
        }
    }

    @Override
    public ResponseEntity<?> get3LatestTrips() {
        Set<TripResponse> trips = tripRepository.getTop3ByOrderByCreatedAtDesc();
        if (trips == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk ekziston asnje udhetim aktiv");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(trips);
        }
    }

    @Override
    public ResponseEntity<?> getFilteredTrips(int page, int size, String startCity, String endCity, String date) {
        Pageable pageable = PageRequest.of(page, size);
        List<TripResponse> filteredTrips = tripRepository.findAllByStartCityAndEndCityAndDate(startCity, endCity, LocalDate.parse(date), pageable).stream().toList();

        if (filteredTrips == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk ekziston asnje udhetim aktiv");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(filteredTrips);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> cancelTrip(Long tripId) {
        Trip tripToBeCanceled = tripRepository.findById(tripId).orElse(null);
        User authenticatedUser = userService.getAuthenticatedUser();
        System.out.println(authenticatedUser.getId());

        if (tripToBeCanceled == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Udhetimi qe doni te anuloni nuk ekziston");
        }

        if (!tripToBeCanceled.getDriver().getId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ju nuk mund te anuloni udhetimin e dikujt tjeter");
        }

        LocalDateTime tripStartTime = LocalDateTime.of(tripToBeCanceled.getDate(), tripToBeCanceled.getTime());

        if (LocalDateTime.now().plusHours(2).isAfter(tripStartTime)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Udhetimi nuk mund te anulohet");
        }

        Set<Booking> bookings = tripToBeCanceled.getBookings();
        if (!bookings.isEmpty()) {
            bookings.forEach(booking -> booking.setBookingStatus(BookingStatus.TRIP_CANCELED));
            bookingRepository.saveAll(bookings);
        }

        tripToBeCanceled.setTripStatus(TripStatus.CANCELED);
        tripRepository.save(tripToBeCanceled);

        return ResponseEntity.status(HttpStatus.OK).body("Udhetimi juaj u anulua");
    }

    @Override
    public ResponseEntity<?> payForTrip(Long id) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Trip activeTripOfAuthenticatedUser = tripRepository.findById(id).orElse(null);
        Booking bookingOfUser = bookingRepository.findBookingByPassengerAndTrip(authenticatedUser, activeTripOfAuthenticatedUser);

        if (activeTripOfAuthenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Udhetimi qe doni te paguani nuk ekziston");
        }

        double totalPrice = bookingOfUser.getReservedSeats() * activeTripOfAuthenticatedUser.getPricePerSeat();

        return transactionService.createTransaction(authenticatedUser, activeTripOfAuthenticatedUser, totalPrice);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void startScheduledTrips() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().withSecond(0).withNano(0);
        List<Trip> tripsToStart = tripRepository.findByTripStatusAndDateAndTime(TripStatus.CREATED, date, time);

        for (Trip trip : tripsToStart) {
            Set<TripApplication> pendingApplications = trip.getTripApplications().stream().filter(tripApplication -> tripApplication.getStatus().equals(ApplicationStatus.PENDING)).collect(Collectors.toSet());
            pendingApplications.forEach(pendingApplication -> pendingApplication.setStatus(ApplicationStatus.REJECTED));
            tripApplicationRepository.saveAll(pendingApplications);
            trip.setTripStatus(TripStatus.STARTED);
            tripRepository.save(trip);
        }

    }
}

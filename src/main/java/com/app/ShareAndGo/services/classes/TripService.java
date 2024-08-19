package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.TripCreationRequest;
import com.app.ShareAndGo.dto.responses.TripResponse;
import com.app.ShareAndGo.entities.*;
import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.enums.BookingStatus;
import com.app.ShareAndGo.enums.BookingType;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripService implements ITripService {

    private final IUserService userService;
    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;
    private final ITransactionService transactionService;
    private final TripApplicationRepository tripApplicationRepository;
    private final CarRepository carRepository;
    private final UserProfileRepository userProfileRepository;

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
                .tripStatus(TripStatus.CREATED)
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
        if (tripResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Udhetimi nuk ekziston");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tripResponse);
    }

    @Override
    public ResponseEntity<?> getTripsAsDriver() {
        User authenticatedUser = userService.getAuthenticatedUser();
        Set<TripResponse> tripResponses = tripRepository.getTripsByDriverAndTripStatus(authenticatedUser, TripStatus.FINISHED);

        if (tripResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk keni asnje udhetim si shofer");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
    }

    @Override
    public ResponseEntity<?> getTripsAsPassenger() {
        User authenticatedUser = userService.getAuthenticatedUser();

        Set<Trip> allTrips = tripRepository.findAllByTripStatus(TripStatus.FINISHED);

        Set<Trip> tripsAsPassenger = allTrips.stream()
                .filter(trip ->
                        trip.getBookings().stream().anyMatch(booking ->
                                Objects.equals(booking.getPassenger().getId(), authenticatedUser.getId())))
                .collect(Collectors.toSet());

        if (tripsAsPassenger.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk keni asnje udhetim si pasagjer");
        }

        Set<TripResponse> tripResponses = tripsAsPassenger.stream().map(trip -> new TripResponse() {
            @Override
            public Long getId() {
                return trip.getId();
            }

            @Override
            public String getStartCity() {
                return trip.getStartCity();
            }

            @Override
            public String getEndCity() {
                return trip.getEndCity();
            }

            @Override
            public LocalDate getDate() {
                return trip.getDate();
            }

            @Override
            public LocalTime getTime() {
                return trip.getTime();
            }

            @Override
            public double getPricePerSeat() {
                return trip.getPricePerSeat();
            }

            @Override
            public double getDuration() {
                return trip.getDuration();
            }

            @Override
            public double getDistance() {
                return trip.getDistance();
            }

            @Override
            public int getAvailableSeats() {
                return trip.getAvailableSeats();
            }

            @Override
            public int getTotalSeats() {
                return trip.getTotalSeats();
            }

            @Override
            public Long getDriverId() {
                return trip.getDriver().getId();
            }

            @Override
            public String getDriverFirstname() {
                return trip.getDriver().getProfile().getFirstname();
            }

            @Override
            public String getDriverLastname() {
                return trip.getDriver().getProfile().getLastname();
            }

            @Override
            public String getDriverProfilePictureURL() {
                return trip.getDriver().getProfile().getProfilePictureUrl();
            }

            @Override
            public Long getCarId() {
                return trip.getCar().getId();
            }
        }).collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
    }

    @Override
    public ResponseEntity<?> endActiveTripOfAuthenticatedUser() {
        User authenticatedUser = userService.getAuthenticatedUser();

        Trip activeTripOfAuthenticatedUser = tripRepository.getTripByDriverAndTripStatus(authenticatedUser, TripStatus.STARTED);

        if (activeTripOfAuthenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ju nuk keni nje udhetim aktiv");
        }

        double totalEarned = activeTripOfAuthenticatedUser.getPricePerSeat() * (activeTripOfAuthenticatedUser.getTotalSeats() - activeTripOfAuthenticatedUser.getAvailableSeats());
        activeTripOfAuthenticatedUser.setTripStatus(TripStatus.FINISHED);
        tripRepository.save(activeTripOfAuthenticatedUser);

        int packagesDelivered = (int) activeTripOfAuthenticatedUser.getBookings().stream().filter(booking -> booking.getBookingType().equals(BookingType.PACKAGE_ONLY) || booking.getBookingType().equals(BookingType.PASSENGER_WITH_PACKAGE)).count();

        UserProfile profile = authenticatedUser.getProfile();
        profile.setPackagesDelivered(profile.getPackagesDelivered() + packagesDelivered);
        profile.setTripsOffered(profile.getTripsOffered() + 1);

        Set<Booking> bookings = activeTripOfAuthenticatedUser.getBookings();
        bookings.forEach(booking -> {
            UserProfile passengerProfile = booking.getPassenger().getProfile();
            BookingType bookingType = booking.getBookingType();

            if (bookingType.equals(BookingType.PACKAGE_ONLY)){
                passengerProfile.setPackagesSent(passengerProfile.getPackagesSent() + 1);
            } else if (bookingType.equals(BookingType.PASSENGER_WITH_PACKAGE)) {
                passengerProfile.setPackagesSent(passengerProfile.getPackagesSent() + 1);
                passengerProfile.setTripsReceived(passengerProfile.getTripsReceived() + 1);
            } else {
                passengerProfile.setTripsReceived(passengerProfile.getTripsReceived() + 1);
            }

            userProfileRepository.save(profile);
        });
        return ResponseEntity.status(HttpStatus.OK).body("Ju fituat " + totalEarned + "ALL nga ky udhetim");
    }

    @Override
    public ResponseEntity<?> getActiveTripsAsDriver() {
        User authenticatedUser = userService.getAuthenticatedUser();

        Set<TripResponse> activeTripsAsDriver = tripRepository.getTripsByDriverAndTripStatus(authenticatedUser, TripStatus.CREATED);
        if (activeTripsAsDriver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk keni asnje udhetim si shofer");
        }
        return ResponseEntity.status(HttpStatus.OK).body(activeTripsAsDriver);
    }

    @Override
    public ResponseEntity<?> getActiveTripsAsPassenger() {
        User authenticatedUser = userService.getAuthenticatedUser();

        Set<Trip> activeTripsAsPassenger = authenticatedUser.getBookings()
                .stream()
                .filter(booking -> booking.getTrip().getTripStatus().equals(TripStatus.CREATED)
                ).collect(Collectors.toSet())
                .stream()
                .map(Booking::getTrip)
                .collect(Collectors.toSet());

        Set<TripResponse> tripResponses = activeTripsAsPassenger.stream().map(trip -> new TripResponse() {
            @Override
            public Long getId() {
                return trip.getId();
            }

            @Override
            public String getStartCity() {
                return trip.getStartCity();
            }

            @Override
            public String getEndCity() {
                return trip.getEndCity();
            }

            @Override
            public LocalDate getDate() {
                return trip.getDate();
            }

            @Override
            public LocalTime getTime() {
                return trip.getTime();
            }

            @Override
            public double getPricePerSeat() {
                return trip.getPricePerSeat();
            }

            @Override
            public double getDuration() {
                return trip.getDuration();
            }

            @Override
            public double getDistance() {
                return trip.getDistance();
            }

            @Override
            public int getAvailableSeats() {
                return trip.getAvailableSeats();
            }

            @Override
            public int getTotalSeats() {
                return trip.getTotalSeats();
            }

            @Override
            public Long getDriverId() {
                return trip.getDriver().getId();
            }

            @Override
            public String getDriverFirstname() {
                return trip.getDriver().getProfile().getFirstname();
            }

            @Override
            public String getDriverLastname() {
                return trip.getDriver().getProfile().getLastname();
            }

            @Override
            public String getDriverProfilePictureURL() {
                return trip.getDriver().getProfile().getProfilePictureUrl();
            }

            @Override
            public Long getCarId() {
                return trip.getCar().getId();
            }
        }).collect(Collectors.toSet());
        if (tripResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk keni asnje udhetim si pasagjer");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tripResponses);
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    public ResponseEntity<?> getAllTrips(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<TripResponse> trips = tripRepository.findAllByTripStatus(pageable, TripStatus.CREATED).stream().toList();

        if (trips.isEmpty()) {
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
        List<TripResponse> filteredTrips = tripRepository.findAllByStartCityAndEndCityAndDateAndTripStatus(startCity, endCity, LocalDate.parse(date), pageable, TripStatus.CREATED).stream().toList();

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

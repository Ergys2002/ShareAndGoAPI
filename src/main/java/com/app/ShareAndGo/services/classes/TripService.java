package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.TripCreationRequest;
import com.app.ShareAndGo.dto.responses.TripResponse;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.enums.TripType;
import com.app.ShareAndGo.repositories.TripRepository;
import com.app.ShareAndGo.repositories.UserRepository;
import com.app.ShareAndGo.services.interfaces.ITripService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TripService implements ITripService {

    private final IUserService userService;
    private final TripRepository tripRepository;
    @Override
    public ResponseEntity<?> createTrip(TripCreationRequest tripData) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Set<Trip> tripsOfAuthenticatedDriver = tripRepository.findAllByDriver(authenticatedUser);

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
                .build();

        boolean hasInterferingTrips = tripsOfAuthenticatedDriver.stream().anyMatch(trip -> checkOverlap(trip, newTrip));

        if(LocalDateTime.parse(tripData.getDateOfTrip() + "T" + tripData.getTimeOfTrip()).isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Koha e udhetimit tuaj nuk mund te jete ne te shkuaren");
        }else {
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

    private boolean checkOverlap(Trip existingTrip, Trip newTrip){
        LocalTime startTimeOfExistingTrip = existingTrip.getTime();
        LocalTime startTimeOfNewTrip = newTrip.getTime();
        LocalTime endTimeOfExistingTrip = existingTrip.getTime().plusMinutes((long) existingTrip.getDuration() * 60);
        LocalTime endTimeOfNewTrip = newTrip.getTime().plusMinutes((long) newTrip.getDuration() * 60);

        if(!existingTrip.getDate().isEqual(newTrip.getDate())){
            return false;
        }else {
            return startTimeOfNewTrip.isBefore(endTimeOfExistingTrip) && endTimeOfNewTrip.isAfter(startTimeOfExistingTrip);
        }
    }
    public Trip getLatestTripOfAuthenticatedUser(){
        return tripRepository.findFirstByDriverOrderByIdDesc(userService.getAuthenticatedUser());
    }

    public Trip getTripById(Long id){
        return tripRepository.findById(id).orElse(null);
    }

    public Trip saveTrip(Trip trip){
        return tripRepository.save(trip);
    }

    @Override
    public ResponseEntity<?> getAllTrips() {

        Set<TripResponse> trips = tripRepository.getAll();
        if(trips == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk ekziston asnje udhetim aktiv");
        } else{
            return ResponseEntity.status(HttpStatus.OK).body(trips);
        }
    }
}

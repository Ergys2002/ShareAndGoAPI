package com.app.ShareAndGo.controllers;


import com.app.ShareAndGo.dto.requests.TripCreationRequest;
import com.app.ShareAndGo.services.interfaces.ITripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final ITripService tripService;

    @PostMapping("/create-trip")
    public ResponseEntity<?> createTrip(@RequestBody TripCreationRequest tripData){
        return tripService.createTrip(tripData);
    }

    @PutMapping("/cancel-trip")
    public ResponseEntity<?> cancelTrip(@RequestParam("id") Long tripId){
        return tripService.cancelTrip(tripId);
    }

    @GetMapping("/all-trips")
    public ResponseEntity<?> getAllTrips(@RequestParam("page") int page, @RequestParam("size") int size){
        return tripService.getAllTrips(page, size);
    }

    @GetMapping("/filtered-trips")
    public ResponseEntity<?> getFilteredTrips(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("startCity") String startCity,
            @RequestParam("endCity") String endCity,
            @RequestParam("date") String date
    ){
        return tripService.getFilteredTrips(page, size, startCity, endCity, date);
    }


    @GetMapping("/3-latest")
    public ResponseEntity<?> get3LatestTrips(){
        return tripService.get3LatestTrips();
    }

    @PutMapping("/pay-for-trip")
    public ResponseEntity<?> payForTrip(@RequestParam("id") Long id){
        return tripService.payForTrip(id);
    }
}

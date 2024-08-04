package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.TripApplicationRequest;
import com.app.ShareAndGo.entities.Package;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripApplication;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.enums.ApplicationType;
import com.app.ShareAndGo.enums.BookingType;
import com.app.ShareAndGo.repositories.PackageRepository;
import com.app.ShareAndGo.repositories.TripApplicationRepository;
import com.app.ShareAndGo.services.interfaces.ITripApplicationService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripApplicationService  implements ITripApplicationService {
    private final TripApplicationRepository tripApplicationRepository;
    private final TripService tripService;
    private final IUserService userService;
    private final PackageRepository packageRepository;

    @Override
    @Transactional
    public ResponseEntity<?> applyForTripReservation(TripApplicationRequest tripApplicationRequest) {
        Trip trip = tripService.getTripById(tripApplicationRequest.getTripId());
        User authenticatedUser = userService.getAuthenticatedUser();
        if(trip == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Udhetimi per te cilin doni te aplikoni nuk ekziston");
        }

        if (trip.getAvailableSeats() < tripApplicationRequest.getNumberOfSeats()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nuk ka mjaftueshem vende");
        }

        if (tripApplicationRequest.getApplicationType().equals(BookingType.PASSENGER_ONLY.toString())){
            return handlePassengerOnlyApplication(tripApplicationRequest, trip, authenticatedUser);
        } else if (tripApplicationRequest.getApplicationType().equals(BookingType.PACKAGE_ONLY.toString())) {
            return handlePackageOnlyApplication(tripApplicationRequest, trip, authenticatedUser);
        } else if (tripApplicationRequest.getApplicationType().equals(BookingType.PASSENGER_WITH_PACKAGE.toString())) {
            return handlePassengerWithPackageApplication(tripApplicationRequest, trip, authenticatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipi i rezervimit i pasakte");
        }
    }

    private ResponseEntity<?> handlePassengerOnlyApplication(TripApplicationRequest request , Trip trip, User user){
        TripApplication tripApplication = TripApplication.builder()
                .applicationType(ApplicationType.PASSENGER_ONLY)
                .status(ApplicationStatus.PENDING)
                .trip(trip)
                .applicant(user)
                .numberOfSeats(request.getNumberOfSeats())
                .build();

        tripApplicationRepository.save(tripApplication);

        return ResponseEntity.status(HttpStatus.OK).body("Aplikimi juaj u ruajt me sukses");
    }
    private ResponseEntity<?> handlePackageOnlyApplication(TripApplicationRequest request, Trip trip, User user){
        TripApplication tripApplication = TripApplication.builder()
                .applicationType(ApplicationType.PACKAGE_ONLY)
                .status(ApplicationStatus.PENDING)
                .trip(trip)
                .applicant(user)
                .build();

        Set<Package> packages = request.getPackages().stream()
                .map(packageRequest -> Package.builder()
                        .sender(user)
                        .length(packageRequest.getLength())
                        .weight(packageRequest.getWeight())
                        .width(packageRequest.getWidth())
                        .height(packageRequest.getHeight())
                        .receiverPhoneNumber(packageRequest.getReceiverPhoneNumber())
                        .tripApplication(tripApplication)
                        .trip(trip)
                        .build()
                ).collect(Collectors.toSet());

        tripApplication.setPackages(packages);

        tripApplicationRepository.save(tripApplication);
        packageRepository.saveAll(packages);

        return ResponseEntity.status(HttpStatus.OK).body("Aplikimi juaj u ruajt me sukses");
    }
    private ResponseEntity<?> handlePassengerWithPackageApplication(TripApplicationRequest request, Trip trip, User user){
        TripApplication tripApplication = TripApplication.builder()
                .applicationType(ApplicationType.PASSENGER_WITH_PACKAGE)
                .trip(trip)
                .numberOfSeats(request.getNumberOfSeats())
                .status(ApplicationStatus.PENDING)
                .applicant(user)
                .build();

        Set<Package> packages = request.getPackages().stream()
                .map(packageRequest -> Package.builder()
                        .sender(user)
                        .length(packageRequest.getLength())
                        .weight(packageRequest.getWeight())
                        .width(packageRequest.getWidth())
                        .height(packageRequest.getHeight())
                        .receiverPhoneNumber(packageRequest.getReceiverPhoneNumber())
                        .tripApplication(tripApplication)
                        .trip(trip)
                        .build()
                ).collect(Collectors.toSet());

        tripApplication.setPackages(packages);

        tripApplicationRepository.save(tripApplication);
        packageRepository.saveAll(packages);

        return ResponseEntity.status(HttpStatus.OK).body("Aplikimi juaj u ruajt me sukses");
    }
}

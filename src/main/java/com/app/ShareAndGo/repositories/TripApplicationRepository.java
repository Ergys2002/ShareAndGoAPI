package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.dto.responses.TripApplicationResponse;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripApplication;
import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.enums.ApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TripApplicationRepository extends JpaRepository<TripApplication, Long> {
    Set<TripApplication> findTripApplicationsByTripAndStatus(Trip trip, ApplicationStatus applicationStatus);

    Set<TripApplicationResponse> findTripApplicationsByTrip(Trip trip);
}

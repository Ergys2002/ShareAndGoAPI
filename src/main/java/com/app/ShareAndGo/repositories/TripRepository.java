package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.dto.responses.TripResponse;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripApplication;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.enums.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Trip findFirstByDriverOrderByIdDesc(User driver);
    Set<Trip> findAllByDriver(User driver);

    @Query("from Trip t order by t.createdAt desc limit 3")
    Set<TripResponse> getTop3ByOrderByCreatedAtDesc();

    Page<TripResponse> findAllByTripStatus(Pageable pageable, TripStatus tripStatus);

    Page<TripResponse> findAllByStartCityAndEndCityAndDateAndTripStatus(String startCity, String endCity, LocalDate date, Pageable pageable, TripStatus tripStatus);
    TripResponse getTripById(Long id);

    List<Trip> findByTripStatusAndDateAndTime(TripStatus tripStatus, LocalDate date, LocalTime time);

    Set<TripResponse> getTripsByDriverAndTripStatus(User driver, TripStatus tripStatus);

    Set<Trip> findAllByTripStatus(TripStatus tripStatus);

    Trip getTripByDriverAndTripStatus(User driver, TripStatus tripStatus);

}

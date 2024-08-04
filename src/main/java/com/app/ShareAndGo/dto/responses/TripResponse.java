package com.app.ShareAndGo.dto.responses;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalTime;

public interface TripResponse {
    Long getId();
    String getStartCity();
    String getEndCity();
    LocalDate getDate();
    LocalTime getTime();
    double getPricePerSeat();
    double getDuration();
    double getDistance();
    int getAvailableSeats();
    int getTotalSeats();

    @Value("#{target.driver.id}")
    Long getDriverId();

    @Value("#{target.driver.profile.firstname}")
    String getDriverFirstname();

    @Value("#{target.driver.profile.lastname}")
    String getDriverLastname();

    @Value("#{target.driver.profile.profilePictureUrl}")
    String getDriverProfilePictureURL();
}

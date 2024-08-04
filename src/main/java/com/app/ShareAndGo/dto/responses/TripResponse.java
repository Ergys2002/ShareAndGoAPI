package com.app.ShareAndGo.dto.responses;

import com.app.ShareAndGo.enums.TripType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalTime;

public interface TripResponse {
    Long getId();
    String getStartCity();
    String getEndCity();
    LocalDate getDate();
    LocalTime getTime();
    double getDuration();
    double getDistance();
    int getAvailableSeats();
    TripType getTripType();

    @Value("#{target.driver.id}")
    Long getDriverId();
}

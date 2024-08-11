package com.app.ShareAndGo.dto.responses;

import com.app.ShareAndGo.enums.BookingStatus;
import com.app.ShareAndGo.enums.BookingType;
import org.springframework.beans.factory.annotation.Value;

public interface BookingResponse {
    Long getId();
    BookingType getBookingType();
    BookingStatus getBookingStatus();
    int getReservedSeats();

    @Value("#{target.passenger.id}")
    Long getApplicantId();
    @Value("#{target.passenger.profile.firstname}")
    String getApplicantFirstname();
    @Value("#{target.passenger.profile.lastname}")
    String getApplicantLastname();
    @Value("#{target.passenger.profile.profilePictureUrl}")
    String getApplicantProfilePictureUrl();
}

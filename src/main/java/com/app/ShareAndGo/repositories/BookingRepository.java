package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.Booking;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long > {
    Booking findBookingByPassengerAndTrip(User passenger, Trip trip);
}

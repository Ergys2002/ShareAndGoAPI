package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long > {
}

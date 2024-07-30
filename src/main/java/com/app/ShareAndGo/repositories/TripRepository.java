package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripApplication;
import com.app.ShareAndGo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Trip findFirstByDriverOrderByIdDesc(User driver);
    Set<Trip> findAllByDriver(User driver);
}
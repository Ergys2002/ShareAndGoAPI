package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.TripApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripApplicationRepository extends JpaRepository<TripApplication, Long> {
}

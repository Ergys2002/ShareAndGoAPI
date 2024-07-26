package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.TripFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripFeedbackRepository extends JpaRepository<TripFeedback, Long> {
}

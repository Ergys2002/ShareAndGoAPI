package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.TripPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripPreferenceRepository extends JpaRepository<TripPreference, Long> {
}

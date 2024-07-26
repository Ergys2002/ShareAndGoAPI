package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.TripLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripLogRepository extends JpaRepository<TripLog, Long> {
}

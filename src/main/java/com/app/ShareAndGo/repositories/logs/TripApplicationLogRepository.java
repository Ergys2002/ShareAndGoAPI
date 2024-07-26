package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.TripApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripApplicationLogRepository extends JpaRepository<TripApplicationLog, Long> {
}

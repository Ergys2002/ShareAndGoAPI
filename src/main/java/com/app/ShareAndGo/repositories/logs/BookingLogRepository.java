package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingLogRepository extends JpaRepository<BookingLog, Long> {
}

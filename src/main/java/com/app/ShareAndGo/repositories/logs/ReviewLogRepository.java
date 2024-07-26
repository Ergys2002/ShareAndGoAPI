package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.ReviewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLogRepository extends JpaRepository<ReviewLog, Long> {
}

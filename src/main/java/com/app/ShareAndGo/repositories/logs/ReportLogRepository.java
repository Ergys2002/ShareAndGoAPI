package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {
}

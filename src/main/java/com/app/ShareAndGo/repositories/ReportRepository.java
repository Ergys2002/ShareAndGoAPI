package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.dto.responses.ReportResponse;
import com.app.ShareAndGo.entities.Report;
import com.app.ShareAndGo.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Set<Report> findReportsByCreatedAtBetweenAndReportStatus(LocalDateTime begin, LocalDateTime end, ReportStatus reportStatus);
}

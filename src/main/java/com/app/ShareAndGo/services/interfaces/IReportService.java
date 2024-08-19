package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.ReportRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface IReportService {
    ResponseEntity<?> createReport(ReportRequest request);

    ResponseEntity<?> getUncheckedReviewsFilteredByDate(LocalDate begin, LocalDate end);

    ResponseEntity<?> checkReport(Long reportId);
}

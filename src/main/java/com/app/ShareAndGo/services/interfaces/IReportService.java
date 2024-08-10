package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.ReportRequest;
import org.springframework.http.ResponseEntity;

public interface IReportService {
    ResponseEntity<?> createReport(ReportRequest request);
}

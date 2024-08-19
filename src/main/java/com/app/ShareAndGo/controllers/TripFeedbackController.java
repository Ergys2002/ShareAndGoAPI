package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.ReportRequest;
import com.app.ShareAndGo.dto.requests.ReviewRequest;
import com.app.ShareAndGo.services.interfaces.IReportService;
import com.app.ShareAndGo.services.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class TripFeedbackController {

    private final IReviewService reviewService;
    private final IReportService reportService;

    @PostMapping("/review/leave-review")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request) {
        return reviewService.createReview(request);
    }

    @PostMapping("/report/create-report")
    public ResponseEntity<?> createReport(@RequestBody ReportRequest request) {
        return reportService.createReport(request);
    }

    @GetMapping("/review/by-trip")
    public ResponseEntity<?> getReviewsByTripId(@RequestParam("id") Long tripId) {
        return reviewService.getReviewsByTripId(tripId);
    }

    @GetMapping("/admin/report/un-checked")
    public ResponseEntity<?> getUncheckedReportsFilteredByDate(@RequestParam("begin") LocalDate begin, @RequestParam("end") LocalDate end) {
        return reportService.getUncheckedReviewsFilteredByDate(begin, end);
    }

    @PutMapping("/admin/check-report")
    public ResponseEntity<?> checkReport(@RequestParam("id") Long reportId){
        return reportService.checkReport(reportId);
    }
}

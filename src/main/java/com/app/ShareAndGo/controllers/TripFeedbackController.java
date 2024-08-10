package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.ReportRequest;
import com.app.ShareAndGo.dto.requests.ReviewRequest;
import com.app.ShareAndGo.services.interfaces.IReportService;
import com.app.ShareAndGo.services.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class TripFeedbackController {

    private final IReviewService reviewService;
    private final IReportService reportService;

    @PostMapping("/review/leave-review")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request){
        return reviewService.createReview(request);
    }

    @PostMapping("/report/create-report")
    public ResponseEntity<?> createReport(@RequestBody ReportRequest request){
        return reportService.createReport(request);
    }
}

package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.ReportRequest;
import com.app.ShareAndGo.entities.Report;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripFeedback;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.enums.FeedbackType;
import com.app.ShareAndGo.enums.ReportPurpose;
import com.app.ShareAndGo.repositories.ReportRepository;
import com.app.ShareAndGo.repositories.TripFeedbackRepository;
import com.app.ShareAndGo.repositories.TripRepository;
import com.app.ShareAndGo.services.interfaces.IReportService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final IUserService userService;
    private final TripRepository tripRepository;
    private final TripFeedbackRepository tripFeedbackRepository;
    private final ReportRepository reportRepository;
    @Override
    public ResponseEntity<?> createReport(ReportRequest request) {
        User reporter = userService.getAuthenticatedUser();

        if (request.getRecipientId().equals(reporter.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ju nuk mund te lini nje raportim per veten tuaj");
        }

        Trip trip = tripRepository.findById(request.getTripId()).orElse(null);
        User recipient = userService.getUserById(request.getRecipientId());

        if (trip == null || recipient == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem me udhetimin ose marresin e raportimit");
        }

        TripFeedback tripFeedback = tripFeedbackRepository.findTripFeedbackByTripAndReviewerAndRecipientAndFeedbackType(trip,reporter,recipient, FeedbackType.REPORT);

        if(tripFeedback != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tashme keni raportuar per kete perdorues");
        }

        Report report = Report.builder()
                .reportPurpose(ReportPurpose.valueOf(request.getReportPurpose()))
                .description(request.getDescription())
                .build();

        reportRepository.save(report);

        TripFeedback feedback = TripFeedback.builder()
                .feedbackType(FeedbackType.REPORT)
                .report(report)
                .reviewer(reporter)
                .recipient(recipient)
                .trip(trip)
                .build();

        tripFeedbackRepository.save(feedback);
        return ResponseEntity.status(HttpStatus.OK).body("Raportimi juaj u ruajt me sukses");
    }
}

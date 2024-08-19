package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.ReportRequest;
import com.app.ShareAndGo.dto.responses.ReportResponse;
import com.app.ShareAndGo.entities.Report;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripFeedback;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.enums.FeedbackType;
import com.app.ShareAndGo.enums.ReportPurpose;
import com.app.ShareAndGo.enums.ReportStatus;
import com.app.ShareAndGo.repositories.ReportRepository;
import com.app.ShareAndGo.repositories.TripFeedbackRepository;
import com.app.ShareAndGo.repositories.TripRepository;
import com.app.ShareAndGo.services.interfaces.IReportService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

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
                .reportPurpose(request.getReportPurpose())
                .description(request.getDescription())
                .reportStatus(ReportStatus.CREATED)
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

    @Override
    public ResponseEntity<?> getUncheckedReviewsFilteredByDate(LocalDate begin, LocalDate end) {
        Set<Report> reports = reportRepository.findReportsByCreatedAtBetweenAndReportStatus(LocalDateTime.of(begin, LocalTime.parse("00:00")), LocalDateTime.of(end, LocalTime.parse("00:00")), ReportStatus.CREATED);

        if (reports.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk ka asnje raportim");
        }

        Set<ReportResponse> reportResponses = reports.stream().map(report -> {

            TripFeedback tripFeedback = report.getTripFeedback();
            User reporter = tripFeedback.getReviewer();
            User recipient = tripFeedback.getRecipient();
            return ReportResponse.builder()
                    .reporterId(reporter.getId())
                    .recipientId(recipient.getId())
                    .reporterName(reporter.getProfile().getFirstname() + " " + reporter.getProfile().getLastname())
                    .recipientName(recipient.getProfile().getFirstname() + " " + recipient.getProfile().getFirstname())
                    .reportId(report.getId())
                    .reportPurpose(report.getReportPurpose())
                    .description(report.getDescription())
                    .reporterProfilePictureUrl(reporter.getProfile().getProfilePictureUrl())
                    .recipientProfilePictureUrl(recipient.getProfile().getProfilePictureUrl())
                    .build();


        }).collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.OK).body(reportResponses);
    }

    @Override
    public ResponseEntity<?> checkReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);

        if (report == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Raportimi nuk u gjet");
        }

        report.setReportStatus(ReportStatus.REVIEWED);
        reportRepository.save(report);

        return ResponseEntity.status(HttpStatus.OK).body("U kontrollua me sukses");
    }
}

package com.app.ShareAndGo.dto.responses;

import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.enums.ApplicationType;
import org.springframework.beans.factory.annotation.Value;

public interface TripApplicationResponse {
    Long getId();
    ApplicationType getApplicationType();
    ApplicationStatus getStatus();
    int getNumberOfSeats();

    @Value("#{target.applicant.id}")
    Long getApplicantId();
    @Value("#{target.applicant.profile.firstname}")
    String getApplicantFirstname();
    @Value("#{target.applicant.profile.lastname}")
    String getApplicantLastname();
    @Value("#{target.applicant.profile.profilePictureUrl}")
    String getApplicantProfilePictureUrl();
}

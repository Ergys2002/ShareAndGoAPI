package com.app.ShareAndGo.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserInfoResponse {
    private int totalReports;
    private int totalReviews;
    private int totalTripsOffered;
    private int totalTripsReceived;
    private double rating;
    private int totalPackagesSent;
    private int totalPackagesDelivered;
}

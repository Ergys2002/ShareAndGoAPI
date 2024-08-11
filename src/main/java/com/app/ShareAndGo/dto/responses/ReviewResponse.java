package com.app.ShareAndGo.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewResponse {
    private Long reviewId;
    private Long reviewerId;
    private Long recipientId;
    private String reviewerName;
    private String recipientName;
    private String reviewerProfilePictureUrl;
    private String recipientProfilePictureUrl;
    private double rating;
    private String comment;
}

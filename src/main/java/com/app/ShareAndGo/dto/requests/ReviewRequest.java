package com.app.ShareAndGo.dto.requests;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ReviewRequest {
    private Long recipientId;
    private Long tripId;
    private double rating;
    private String comment;
}

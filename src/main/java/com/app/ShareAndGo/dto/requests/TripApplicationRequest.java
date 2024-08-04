package com.app.ShareAndGo.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class TripApplicationRequest {
    private String applicationType;
    private Long tripId;
    private int numberOfSeats;
    private List<PackageRequest> packages;
}

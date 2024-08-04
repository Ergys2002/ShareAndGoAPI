package com.app.ShareAndGo.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TripCreationRequest {
    private String startCity;
    private String endCity;
    private String dateOfTrip;
    private String timeOfTrip;
    private double pricePerSeat;
    private int passengerCount;
    private double duration;
    private double distance;
}

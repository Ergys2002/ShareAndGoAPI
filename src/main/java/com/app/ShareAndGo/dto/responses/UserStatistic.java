package com.app.ShareAndGo.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserStatistic {
    private Long userId;
    private int tripsOffered;
    private int tripsReceived;
    private int packagesDelivered;
    private int packagesSent;
}

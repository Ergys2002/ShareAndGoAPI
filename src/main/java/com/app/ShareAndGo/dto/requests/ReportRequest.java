package com.app.ShareAndGo.dto.requests;

import com.app.ShareAndGo.enums.ReportPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ReportRequest {
    private ReportPurpose reportPurpose;
    private String description;
    private Long tripId;
    private Long recipientId;
}

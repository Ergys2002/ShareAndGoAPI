package com.app.ShareAndGo.dto.responses;

import com.app.ShareAndGo.enums.ReportPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReportResponse {
    private Long reportId;
    private Long reporterId;
    private Long recipientId;
    private String reporterName;
    private String recipientName;
    private String reporterProfilePictureUrl;
    private String recipientProfilePictureUrl;
    private ReportPurpose reportPurpose;
    private String description;

}

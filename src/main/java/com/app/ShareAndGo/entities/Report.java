package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.ReportLog;
import com.app.ShareAndGo.enums.ReportPurpose;
import com.app.ShareAndGo.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "report")
public class Report extends BaseEntity{
    @Column(name = "report_purpose")
    @Enumerated(EnumType.STRING)
    private ReportPurpose reportPurpose;
    private String description;
    @Column(name = "report_status")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    @OneToOne(mappedBy = "report")
    private TripFeedback tripFeedback;

    @OneToMany(mappedBy = "report")
    private Set<ReportLog> reportLogs;
}

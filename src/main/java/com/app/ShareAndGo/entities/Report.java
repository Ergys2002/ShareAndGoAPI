package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.ReportLog;
import com.app.ShareAndGo.enums.ReportPurpose;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private ReportPurpose reportPurpose;
    private String description;

    @OneToOne(mappedBy = "report")
    private TripFeedback tripFeedback;

    @OneToMany(mappedBy = "report")
    private Set<ReportLog> reportLogs;
}

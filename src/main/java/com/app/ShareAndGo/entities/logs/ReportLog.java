package com.app.ShareAndGo.entities.logs;

import com.app.ShareAndGo.entities.BaseEntity;
import com.app.ShareAndGo.entities.Booking;
import com.app.ShareAndGo.entities.Report;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "report_log")
@Entity
public class ReportLog extends Log {
    @ManyToOne
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private Report report;
}

package com.app.ShareAndGo.entities.logs;

import com.app.ShareAndGo.entities.BaseEntity;
import com.app.ShareAndGo.entities.Booking;
import com.app.ShareAndGo.entities.TripApplication;
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
@Table(name = "trip_application_log")
@Entity
public class TripApplicationLog extends Log {
    @ManyToOne
    @JoinColumn(name = "trip_application_id", referencedColumnName = "id")
    private TripApplication tripApplication;
}

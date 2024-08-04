package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.TripApplicationLog;
import com.app.ShareAndGo.enums.ApplicationStatus;
import com.app.ShareAndGo.enums.ApplicationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "trip_application")
public class TripApplication extends BaseEntity{

    @Column(name = "application_type")
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @Column(name = "number_of_seats")
    private int numberOfSeats;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "applicant_id", referencedColumnName = "id")
    private User applicant;

    @OneToMany(mappedBy = "tripApplication")
    private Set<Package> packages;

    @OneToMany(mappedBy = "tripApplication")
    private Set<TripApplicationLog> tripApplicationLogs;
}

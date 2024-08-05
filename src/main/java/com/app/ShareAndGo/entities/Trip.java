package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.TripLog;
import com.app.ShareAndGo.enums.TripStatus;
import com.app.ShareAndGo.enums.TripType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@Table(name = "trip")
public class Trip extends BaseEntity{

    @Column(name = "start_city")
    private String startCity;

    @Column(name = "end_city")
    private String endCity;

    private LocalDate date;
    private LocalTime time;
    private double duration;
    private double distance;

    @Column(name = "trip_status")
    @Enumerated(EnumType.STRING)
    private TripStatus tripStatus;

    @Column(name = "price_per_seat")
    private double pricePerSeat;

    @Column(name = "total_seats")
    private int totalSeats;

    @Column(name = "available_seats")
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "driver_id" , referencedColumnName = "id")
    private User driver;

    @OneToMany(mappedBy = "trip")
    private Set<TripApplication> tripApplications;

    @OneToMany(mappedBy = "trip")
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "trip")
    private Set<TripFeedback> tripFeedbacks;

    @OneToMany(mappedBy = "trip")
    private Set<TripPreference> tripPreferences;

    @OneToMany(mappedBy = "trip")
    private Set<Message> messages;

    @OneToMany(mappedBy = "trip")
    private Set<TripLog> tripLogs;
}

package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.enums.BookingStatus;
import com.app.ShareAndGo.enums.BookingType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking")
public class Booking extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType;
    @Column(name = "reserved_seats")
    private int reservedSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @OneToMany(mappedBy = "booking")
    private Set<Package> packages;

    @ManyToOne
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    private User passenger;

    @OneToMany(mappedBy = "booking")
    private Set<BookingLog> bookingLogs;
}

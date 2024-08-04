package com.app.ShareAndGo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "package")
public class Package extends BaseEntity{
    private double weight;
    private double length;
    private double width;
    private double height;
    @Column(name = "receiver_phone_number")
    private String receiverPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "trip_application_id", referencedColumnName = "id")
    private TripApplication tripApplication;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;
}

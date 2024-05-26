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
    @Column(name = "receiver_phone_number")
    private String receiverPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @OneToOne(mappedBy = "aPackage")
    private TripApplication tripApplication;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToOne(mappedBy = "aPackage")
    private Booking booking;
}

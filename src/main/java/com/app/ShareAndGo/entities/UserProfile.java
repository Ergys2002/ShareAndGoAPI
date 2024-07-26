package com.app.ShareAndGo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_profile")
public class UserProfile extends BaseEntity{
    private String firstname;
    private String lastname;
    private String gender;
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private double rating;
    @Column(name = "trips_offered")
    private int tripsOffered;
    @Column(name = "trips_received")
    private int tripsReceived;
    @Column(name = "packages_sent")
    private int packagesSent;
    @Column(name = "packages_delivered")
    private int packagesDelivered;

    @OneToOne(mappedBy = "profile")
    private User user;
}

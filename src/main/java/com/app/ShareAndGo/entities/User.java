package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.UserLog;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity{

    @Column(name = "phone_number")
    private String phoneNumber;
    private String password;
    @Column(name = "account_balance")
    private double accountBalance;
    @Column(name = "is_banned")
    private boolean isBanned;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;

    @OneToMany(mappedBy = "driver")
    private Set<Trip> tripsAsDriver;


    @OneToMany(mappedBy = "applicant")
    private Set<TripApplication> tripApplications;

    @OneToMany(mappedBy = "sender")
    private Set<Package> packages;

    @OneToMany(mappedBy = "owner")
    private Set<Car> cars;

    @OneToMany(mappedBy = "reviewer")
    private Set<TripFeedback> feedbacksGiven;

    @OneToMany(mappedBy = "recipient")
    private Set<TripFeedback> feedbacksReceived;

    @OneToMany(mappedBy = "sender")
    private Set<Transaction> transactionsSending;

    @OneToMany(mappedBy = "recipient")
    private Set<Transaction> transactionsReceiving;

    @OneToMany(mappedBy = "user")
    private Set<PreviousPassword> previousPasswords;

    @OneToMany(mappedBy = "sender")
    private Set<Message> messages;

    @OneToMany(mappedBy = "user")
    private Set<UserLog> userLogs;

}

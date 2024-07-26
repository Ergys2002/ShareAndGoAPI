package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.UserLog;
import com.app.ShareAndGo.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails{

    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;

    @Column(name = "account_balance")
    private double accountBalance;

    private String email;

    @NotBlank(message = "NID is required")
    private String nid;

    private boolean disabled;

    private int salary;

    @Column(name = "is_banned")
    private boolean isBanned;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private Role role;

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBanned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}

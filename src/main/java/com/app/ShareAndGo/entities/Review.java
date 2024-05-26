package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.ReviewLog;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "review")
public class Review extends BaseEntity{
    private double rating;
    private String comment;
    @OneToOne(mappedBy = "review")
    private TripFeedback tripFeedback;

    @OneToMany(mappedBy = "review")
    private Set<ReviewLog> reviewLogs ;
}

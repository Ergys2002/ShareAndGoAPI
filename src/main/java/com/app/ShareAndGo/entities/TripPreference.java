package com.app.ShareAndGo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "trip_preference")
public class TripPreference extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "preference_id", referencedColumnName = "id")
    private Preference preference;


    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;
}

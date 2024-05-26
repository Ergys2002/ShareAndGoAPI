package com.app.ShareAndGo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Table(name = "location")
@Entity
public class Location extends BaseEntity{
    private double latitude;
    private double longitude;
}

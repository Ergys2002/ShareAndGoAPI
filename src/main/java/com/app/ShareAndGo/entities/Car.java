package com.app.ShareAndGo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "car")
public class Car extends BaseEntity{
    private String make;
    private String model;
    @Column(name = "licence_plate_number")
    private String licencePlateNumber;
    @Column(name = "car_image_url")
    private String carImageURL;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
}

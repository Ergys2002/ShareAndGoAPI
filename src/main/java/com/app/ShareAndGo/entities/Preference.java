package com.app.ShareAndGo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "preference")
public class Preference extends BaseEntity{
    private String title;
    private int preferenceUsage;
}

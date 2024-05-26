package com.app.ShareAndGo.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    protected LocalDate createdAt;


    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDate.now();
    }
}

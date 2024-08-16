package com.app.ShareAndGo.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @Column(name = "id", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    protected LocalDateTime createdAt;


    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }
}

package com.app.ShareAndGo.entities.logs;

import com.app.ShareAndGo.entities.BaseEntity;
import com.app.ShareAndGo.entities.Booking;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "booking_log")
@Entity
public class BookingLog extends Log {
    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;
}

package com.app.ShareAndGo.entities.logs;

import com.app.ShareAndGo.entities.BaseEntity;
import com.app.ShareAndGo.entities.Booking;
import com.app.ShareAndGo.entities.PreviousPassword;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "password_log")
@Entity
public class PasswordLog extends Log {
    @ManyToOne
    @JoinColumn(name = "password_id", referencedColumnName = "id")
    private PreviousPassword previousPassword;
}

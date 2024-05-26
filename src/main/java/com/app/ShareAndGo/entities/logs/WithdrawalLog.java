package com.app.ShareAndGo.entities.logs;


import com.app.ShareAndGo.entities.BaseEntity;
import com.app.ShareAndGo.entities.Booking;
import com.app.ShareAndGo.entities.WithdrawalRequest;
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
@Table(name = "withdrawal_log")
@Entity
public class WithdrawalLog extends Log {
    @ManyToOne
    @JoinColumn(name = "withdrawal_request_id", referencedColumnName = "id")
    private WithdrawalRequest withdrawalRequest;
}

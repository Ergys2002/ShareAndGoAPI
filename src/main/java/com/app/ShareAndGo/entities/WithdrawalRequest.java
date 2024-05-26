package com.app.ShareAndGo.entities;


import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.WithdrawalLog;
import com.app.ShareAndGo.enums.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "withdrawal_request")
public class WithdrawalRequest extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "withdrawal_status")
    private WithdrawalStatus withdrawalStatus;

    private double amount;

    @OneToMany(mappedBy = "withdrawalRequest")
    private Set<WithdrawalLog> withdrawalLogs;
}

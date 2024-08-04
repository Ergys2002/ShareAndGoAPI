package com.app.ShareAndGo.entities;


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
@Table(name = "withdrawal")
public class Withdrawal extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "withdrawal_status")
    private WithdrawalStatus withdrawalStatus;

    @Column(name = "account_number")
    private String accountNumber;

    private double amount;

    @OneToMany(mappedBy = "withdrawal")
    private Set<WithdrawalLog> withdrawalLogs;
}

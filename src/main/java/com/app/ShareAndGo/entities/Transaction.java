package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.TransactionLog;
import com.app.ShareAndGo.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transaction")
public class Transaction extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "sender_id" , referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id" , referencedColumnName = "id")
    private User recipient;

    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @OneToMany(mappedBy = "transaction")
    private Set<TransactionLog> transactionLogs;
}

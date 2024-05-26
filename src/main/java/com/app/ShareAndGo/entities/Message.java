package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.MessageLog;
import com.app.ShareAndGo.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "message")
@Entity
public class Message extends BaseEntity{
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "message_content")
    private String messageContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status")
    private MessageStatus messageStatus;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToMany(mappedBy = "message")
    private Set<MessageLog> messageLogs;
}

package com.app.ShareAndGo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder(builderClassName = "MessageBuilder")
@Table(name = "chat")
@Entity
public class Chat extends BaseEntity{
    @Column(name = "chat_identifier")
    private String chatIdentifier;

    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="recipient_id", referencedColumnName = "id")
    private User recipient;

    @OneToMany(mappedBy = "chat")
    private Set<Message> messages;
}

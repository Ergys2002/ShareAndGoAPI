package com.app.ShareAndGo.entities;

import com.app.ShareAndGo.entities.logs.BookingLog;
import com.app.ShareAndGo.entities.logs.MessageLog;
import com.app.ShareAndGo.enums.MessageStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder(builderClassName = "MessageBuilder")
@Table(name = "message")
@Entity
public class Message{

    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "channel")
    private String channel;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "read_date")
    private LocalDateTime readDate;

    public static class MessageBuilder {
        public Message build() {
            Message message = new Message();
            message.sender = this.sender;
            message.receiver = this.receiver;
            message.content = this.content;
            message.timestamp = this.timestamp;
            message.readDate = this.readDate;
            message.channel = generateChannelId(message.sender, message.receiver);
            return message;
        }

        private String generateChannelId(User sender, User receiver) {
            String senderId = String.valueOf(sender.getId());
            String receiverId = String.valueOf(receiver.getId());
            return senderId.compareTo(receiverId) < 0 ? senderId + "-" + receiverId : receiverId + "-" + senderId;
        }
    }
}

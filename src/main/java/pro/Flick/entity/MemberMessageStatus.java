package pro.Flick.entity;

import jakarta.persistence.*;
import pro.Flick.member.entity.Member;

import java.time.LocalDateTime;

@Entity
public class MemberMessageStatus {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;


    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private LocalDateTime readAt;

    public MemberMessageStatus() {
    }

    public MemberMessageStatus(Message message, Member member, ChatRoom chatRoom, MessageStatus status, LocalDateTime readAt) {
        this.message = message;
        this.member = member;
        this.chatRoom = chatRoom;
        this.status = status;
        this.readAt = readAt;
    }
}

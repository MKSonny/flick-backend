package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.*;
import pro.Flick.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Message {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    private String text;

    private LocalDateTime createdAt;

    private Boolean read;
}

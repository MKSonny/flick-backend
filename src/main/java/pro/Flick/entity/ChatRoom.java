package pro.Flick.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ChatRoom {
    @Id @GeneratedValue
    private Long id;

    // 채팅방 이름 (예: 그룹 채팅방 제목)
    private String name;

    // 채팅방 생성 시각
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message message;
}
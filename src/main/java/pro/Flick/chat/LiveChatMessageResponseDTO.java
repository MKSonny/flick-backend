package pro.Flick.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.Flick.entity.Message;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class LiveChatMessageResponseDTO {
    private Long senderId;          // 보낸 사람 ID
    private String senderUsername;  // 보낸 사람 이름 (DB 조회 후 설정)
    private String text;            // 메시지 내용
    private LocalDateTime sentAt;   // 보낸 시간

    public LiveChatMessageResponseDTO(Long senderId, String senderUsername, String text, LocalDateTime sentAt) {
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.text = text;
        this.sentAt = sentAt;
    }

    public LiveChatMessageResponseDTO(Message message) {
        this.senderId = message.getSender().getId();
        this.senderUsername = message.getSender().getUsername();
        this.text = message.getText();
        this.sentAt = message.getCreatedAt();
    }
}


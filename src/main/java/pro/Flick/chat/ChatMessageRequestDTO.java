package pro.Flick.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDTO {
    private Long userId;        // 메시지를 보낸 사용자 ID
    private Long chatUserId;    // 메시지를 받는 사용자 ID (프론트에서 recipientId로 사용)
    private String text;        // 메시지 내용
    private String chatRoomId;  // 채팅방 ID

    public ChatMessageRequestDTO(Long userId, Long chatUserId, String text, String chatRoomId) {
        this.userId = userId;
        this.chatUserId = chatUserId;
        this.text = text;
        this.chatRoomId = chatRoomId;
    }
}

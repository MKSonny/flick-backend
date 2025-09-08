package pro.Flick.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatRequestDTO {
    private String senderId; // 보낸 사람의 id
    private String receiverId;
    private String text;
    private Long chatRoomId;

    public ChatRequestDTO(String senderId, String chatUserId, String text, Long chatRoomId) {
        this.senderId = senderId;
        this.receiverId = chatUserId;
        this.text = text;
        this.chatRoomId = chatRoomId;
    }
}

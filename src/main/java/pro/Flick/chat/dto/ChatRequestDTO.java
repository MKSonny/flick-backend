package pro.Flick.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatRequestDTO {
    private String senderId; // 보낸 사람의 id
    private String receiverId;
    private String text;
    private String users_key;

    public ChatRequestDTO(String senderId, String chatUserId, String text, String users_key) {
        this.senderId = senderId;
        this.receiverId = chatUserId;
        this.text = text;
        this.users_key = users_key;
    }
}

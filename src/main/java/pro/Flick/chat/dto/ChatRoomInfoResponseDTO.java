package pro.Flick.chat.dto;

import lombok.Data;

@Data
public class ChatRoomInfoResponseDTO {
    private String chatRoomTitle;

    public ChatRoomInfoResponseDTO(String chatRoomTitle) {
        this.chatRoomTitle = chatRoomTitle;
    }

    public ChatRoomInfoResponseDTO() {
    }
}

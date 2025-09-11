package pro.Flick.chat.dto;

import lombok.Data;

@Data
public class ChatRoomInfoResponseDTO {
    private String chatRoomTitle;
    private String chatRoomProfileImage;

    public ChatRoomInfoResponseDTO(String chatRoomTitle, String chatRoomProfileImage) {
        this.chatRoomTitle = chatRoomTitle;
        this.chatRoomProfileImage = chatRoomProfileImage;
    }

    public ChatRoomInfoResponseDTO() {
    }
}

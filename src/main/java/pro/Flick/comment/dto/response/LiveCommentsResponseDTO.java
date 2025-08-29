package pro.Flick.comment.dto.response;

import lombok.Data;

@Data
public class LiveCommentsResponseDTO {
    private String content;
    private String senderUsername;


    public LiveCommentsResponseDTO(String content, String senderUsername) {
        this.content = content;
        this.senderUsername = senderUsername;
    }
}

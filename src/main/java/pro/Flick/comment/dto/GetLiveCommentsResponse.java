package pro.Flick.comment.dto;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Comment;

import java.time.LocalDateTime;

@Data
public class GetLiveCommentsResponse {
    private String content;
    private String senderUsername;


    public GetLiveCommentsResponse(String content, String senderUsername) {
        this.content = content;
        this.senderUsername = senderUsername;
    }
}

package pro.Flick.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Member;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LiveCommentResponseDTO {
    private Long messageId;
    private Long videoId;
    private Long senderId;
    private String senderUsername;
    private String senderProfileImageUri;
    private String content;
    private LocalDateTime timestamp;
    private boolean isCreator; // 영상 소유자가 보낸 메시지인지 여부

    public LiveCommentResponseDTO(Comment comment, Member member, Long videoId) {
        this.messageId = comment.getId();
        this.videoId = videoId;
        this.senderId = member.getId();
        this.senderUsername = member.getUsername();
        this.senderProfileImageUri = member.getProfileImageUri();
        this.content = comment.getText();
        this.timestamp = comment.getCreatedAt();
        this.isCreator = false;
    }
}

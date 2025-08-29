package pro.Flick.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentAddRequestDTO {
    private Long userId;
    private Long videoId;
    private String text;
    private Long video_user_id;


    public CommentAddRequestDTO(Long userId, Long videoId, String text, Long video_user_id) {
        this.userId = userId;
        this.videoId = videoId;
        this.text = text;
        this.video_user_id = video_user_id;
    }
}

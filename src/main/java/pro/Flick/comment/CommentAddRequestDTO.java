package pro.Flick.comment;

import lombok.Data;

@Data
public class CommentAddRequestDTO {
    private String userId;
    private String videoId;
    private String text;
    private String video_user_id;

    public CommentAddRequestDTO(String userId, String videoId, String text, String video_user_id) {
        this.userId = userId;
        this.videoId = videoId;
        this.text = text;
        this.video_user_id = video_user_id;
    }
}

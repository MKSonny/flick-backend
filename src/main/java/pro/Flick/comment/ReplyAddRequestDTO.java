package pro.Flick.comment;

import lombok.Data;

@Data
public class ReplyAddRequestDTO {
    private Long userId;
    private Long videoId;
    private Long parentId;
    private String text;

    public ReplyAddRequestDTO(Long userId, Long videoId, Long parentId, String text) {
        this.userId = userId;
        this.videoId = videoId;
        this.parentId = parentId;
        this.text = text;
    }
}

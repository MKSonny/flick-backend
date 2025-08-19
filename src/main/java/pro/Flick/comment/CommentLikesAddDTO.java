package pro.Flick.comment;

import lombok.Data;

/*
    userId: user.id,
    commentId: commentId,
    videoId: params.video_id,
    video_user_id: params.video_user_id
 */
@Data
public class CommentLikesAddDTO {
    private Long userId;
    private Long commentId;
    private Long videoId;
    private Long video_user_id;
}

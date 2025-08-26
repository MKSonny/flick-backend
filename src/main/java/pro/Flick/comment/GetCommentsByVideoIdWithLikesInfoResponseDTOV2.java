package pro.Flick.comment;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Comment;

import java.time.LocalDateTime;

@Data
public class GetCommentsByVideoIdWithLikesInfoResponseDTOV2 {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private GetMemberByIdResponseDto user;
    private Long likesCount;
    private Boolean isLikedByUser;
    private Long replyCount;


    public GetCommentsByVideoIdWithLikesInfoResponseDTOV2(Comment comment, Boolean isLikedByUser, Long replyCount) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.createdAt = comment.getCreatedAt();
        this.user = new GetMemberByIdResponseDto(comment.getMember());
        this.likesCount = comment.getLikesCount();
        this.isLikedByUser = isLikedByUser;
        this.replyCount = replyCount;
    }
}

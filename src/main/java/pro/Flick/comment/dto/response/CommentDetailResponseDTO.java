package pro.Flick.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDetailResponseDTO {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private GetMemberByIdResponseDto user;
    private Long likesCount;
    private Boolean isLikedByUser;
    private Long replyCount;


    public CommentDetailResponseDTO(Comment comment, Boolean isLikedByUser, Long replyCount) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.createdAt = comment.getCreatedAt();
        this.user = new GetMemberByIdResponseDto(comment.getMember());
        this.likesCount = comment.getLikesCount();
        this.isLikedByUser = isLikedByUser;
        this.replyCount = replyCount;
    }
}

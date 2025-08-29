package pro.Flick.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Comment;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommentReplyDetailResponseDTO {
    private Long id;
    private String text;
    private GetMemberByIdResponseDto user;
    private LocalDateTime createdAt;
    private Long likesCount;
    private boolean isLikedByUser;

    public CommentReplyDetailResponseDTO(Comment comment, boolean isLikedByUser) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.user = new GetMemberByIdResponseDto(comment.getMember());
        this.createdAt = comment.getCreatedAt();
        this.likesCount = comment.getLikesCount();
        this.isLikedByUser = isLikedByUser;
    }
}

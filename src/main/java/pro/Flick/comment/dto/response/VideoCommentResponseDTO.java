package pro.Flick.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Comment;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class VideoCommentResponseDTO {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private GetMemberByIdResponseDto user;
    private Long likesCount;

    public VideoCommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.createdAt = comment.getCreatedAt();
        this.user = new GetMemberByIdResponseDto(comment.getMember());
        this.likesCount = comment.getLikesCount();
    }
}

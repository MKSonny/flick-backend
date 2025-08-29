package pro.Flick.comment.trash.dto;

import lombok.Data;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Comment;

import java.time.LocalDateTime;

@Data
public class GetCommentsByVideoIdResponseDTO {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private GetMemberByIdResponseDto user;
    private Long likesCount;

    public GetCommentsByVideoIdResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.createdAt = comment.getCreatedAt();
        this.user = new GetMemberByIdResponseDto(comment.getMember());
        this.likesCount = comment.getLikesCount();
    }
}

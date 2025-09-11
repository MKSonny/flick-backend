package pro.Flick.likes.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LikesResponseDto(
        Long id,
        String member_id,
        String video_id,
        String comment_id,
        LocalDateTime createdAt
) {

    public static LikesResponseDto forVideo(Long id, String memberId, String videoId, LocalDateTime createdAt) {
        return new LikesResponseDto(id, memberId, videoId, null, createdAt);
    }

    public static LikesResponseDto forComment(Long id, String memberId, String commentId, LocalDateTime createdAt) {
        return new LikesResponseDto(id, memberId, null, commentId, createdAt);
    }
}

package pro.Flick.likes;

import lombok.Data;

import java.time.LocalDateTime;

@Data
class GetLikesByMemberIdResponseDTO {
    private Long id;
    private Long memberId;
    private Long video_id;
    private LocalDateTime createdAt;

    public GetLikesByMemberIdResponseDTO(Long id, Long memberId, Long video_id, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.video_id = video_id;
        this.createdAt = createdAt;
    }
}

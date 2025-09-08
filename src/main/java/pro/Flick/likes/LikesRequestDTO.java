package pro.Flick.likes;

import lombok.Data;

@Data
class LikesRequestDTO {
    private String userId;
    private String videoId;
    private Long videoUserId;

    public LikesRequestDTO(String userId, String videoId, Long videoUserId) {
        this.userId = userId;
        this.videoId = videoId;
        this.videoUserId = videoUserId;
    }
}

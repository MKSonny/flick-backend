package pro.Flick.likes;

import lombok.Data;

@Data
class LikesRequestDTO {
    private String userId;
    private String videoId;
    private String videoUserId;

    public LikesRequestDTO(String userId, String videoId, String videoUserId) {
        this.userId = userId;
        this.videoId = videoId;
        this.videoUserId = videoUserId;
    }
}

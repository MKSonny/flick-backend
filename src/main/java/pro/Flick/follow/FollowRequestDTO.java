package pro.Flick.follow;

import lombok.Data;

@Data
public class FollowRequestDTO {
    private Long userId;
    private Long follower_user_id;

    public FollowRequestDTO(Long userId, Long follower_user_id) {
        this.userId = userId;
        this.follower_user_id = follower_user_id;
    }
}

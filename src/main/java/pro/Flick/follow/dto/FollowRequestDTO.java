package pro.Flick.follow.dto;

import lombok.Data;

@Data
public class FollowRequestDTO {
//    private Long userId;
    private Long follower_user_id;

    public FollowRequestDTO(Long follower_user_id) {
//        this.userId = userId;
        this.follower_user_id = follower_user_id;
    }
}

package pro.Flick.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowCountDTO {
    private Long followingCount;
    private Long followerCount;
}

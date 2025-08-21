package pro.Flick.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowCountDTO {
    private Long followerCount;
    private Long followingCount;
}

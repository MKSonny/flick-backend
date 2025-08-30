package pro.Flick.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowCountDTO {
    private Long followerCount;
    private Long followingCount;
}

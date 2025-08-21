package pro.Flick.member;

import lombok.Data;
import pro.Flick.entity.Member;

@Data
public class ProfileInfoResponseDTOV2 {
    private String username;
    private String profileImageUrl;
    private Long followingCount;
    private Long followerCount;
    private Long totalLikesCount;
    private Boolean isFollowing;

    public ProfileInfoResponseDTOV2(Member member, Boolean isFollowing, Long followerCount, Long followingCount) {
        this.username = member.getUsername();
        this.profileImageUrl = member.getProfileImageUri();
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.totalLikesCount = 0L;
        this.isFollowing = isFollowing;
    }
}

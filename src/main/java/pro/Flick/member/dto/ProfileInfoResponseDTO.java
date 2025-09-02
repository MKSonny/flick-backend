package pro.Flick.member.dto;

import lombok.Data;
import pro.Flick.entity.Member;

@Data
public class ProfileInfoResponseDTO {
    private String username;
    private String profileImageUrl;
    private Long followingCount;
    private Long followerCount;
    private Long totalLikesCount;
    private Boolean amIFollowing;

    public ProfileInfoResponseDTO(Member member, Long followerCount, Long followingCount, Long totalLikesCount, Boolean amIFollowing) {
        this.username = member.getUsername();
        this.profileImageUrl = member.getProfileImageUri();
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.totalLikesCount = totalLikesCount;
        this.amIFollowing = amIFollowing;
    }
}

package pro.Flick.member;

import lombok.Data;
import pro.Flick.entity.Member;

@Data
public class ProfileInfoResponseDTO {
    private String username;
    private String profileImageUrl;
    private Long followingCount;
    private Long followerCount;
    private Long totalLikesCount;

    public ProfileInfoResponseDTO(Member member, Long followerCount, Long followingCount) {
        this.username = member.getUsername();
        this.profileImageUrl = member.getProfileImageUri();
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.totalLikesCount = 0L;
    }
}

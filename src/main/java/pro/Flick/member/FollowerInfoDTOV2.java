package pro.Flick.member;

import lombok.Data;
import pro.Flick.entity.Member;

@Data
public class FollowerInfoDTOV2 {
    private Long id;
    private String username;
    private String profileImageUrl;
    private Boolean isFollowedByMe;

    public FollowerInfoDTOV2(Member member, Boolean isFollowedByMe) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.profileImageUrl = member.getProfileImageUri();
        this.isFollowedByMe = isFollowedByMe;
    }
}

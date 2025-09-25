package pro.Flick.member.dto;

import lombok.Data;
import pro.Flick.member.entity.Member;

@Data
public class FollowerInfoDTO {
    private Long id;
    private String username;
    private String profileImageUrl;
    private Boolean isFollowedByMe;

    public FollowerInfoDTO(Long id, Member member) {
        this.id = id;
        this.username = member.getUsername();
        this.profileImageUrl = member.getProfileImageUri();
        this.isFollowedByMe = false;
    }
}

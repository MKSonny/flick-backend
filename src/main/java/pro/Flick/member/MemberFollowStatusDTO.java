package pro.Flick.member;

import lombok.Data;
import pro.Flick.entity.Member;

@Data
public class MemberFollowStatusDTO {
    private Member member;
    private boolean isFollowing;


    public MemberFollowStatusDTO(Member member, boolean isFollowing) {
        this.member = member;
        this.isFollowing = isFollowing;
    }
}

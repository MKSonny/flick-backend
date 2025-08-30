package pro.Flick.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pro.Flick.member.dto.FollowerInfoDTO;
import pro.Flick.member.dto.FollowerInfoDTOV2;
import pro.Flick.member.dto.ProfileInfoResponseDTOV2;

public interface MemberRepositoryCustom {

    ProfileInfoResponseDTOV2 QfindMemberProfile(Long myId, Long profileId);
}

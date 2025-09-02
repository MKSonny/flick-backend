package pro.Flick.member.repository;

import pro.Flick.member.dto.LikedVideoResponseDTO;
import pro.Flick.member.dto.ProfileInfoResponseDTOV2;

public interface MemberRepositoryCustom {

    ProfileInfoResponseDTOV2 QfindMemberProfile(Long myId, Long profileId);
}

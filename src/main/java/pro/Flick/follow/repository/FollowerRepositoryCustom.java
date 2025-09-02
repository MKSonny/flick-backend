package pro.Flick.follow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.member.dto.FollowerInfoDTOV2;

public interface FollowerRepositoryCustom {
    Page<FollowerInfoDTOV2> QfindFollowerByMemberIdV3(Pageable pageable, Long memberId);
}

package pro.Flick.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Member;
import pro.Flick.member.dto.FollowerInfoDTOV2;

public interface FollowerRepositoryCustom {
    Page<FollowerInfoDTOV2> QfindFollowerByMemberIdV3(Pageable pageable, Long memberId);
}

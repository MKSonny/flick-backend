package pro.Flick.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.entity.Member;

public interface FollowerRepositoryCustom {
    Page<Member> findFollowersByMemberId(Pageable pageable, Long memberId);
}

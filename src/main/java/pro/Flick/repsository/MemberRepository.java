package pro.Flick.repsository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 올바르게 수정된 쿼리
    @Query("SELECT f.follower FROM Follower f WHERE f.member.id = :memberId")
    Page<Member> findFollowersByMemberId(Pageable pageable, @Param("memberId") Long memberId);
}

package pro.Flick.repsository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Follower;
import pro.Flick.member.FollowCountDTO;

public interface FollowRepository extends JpaRepository<Follower, Long> {


    @Query("SELECT new pro.Flick.member.FollowCountDTO(" +
            "SUM(CASE WHEN f.follower.id = :memberId THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN f.member.id = :memberId THEN 1 ELSE 0 END)) " +
            "FROM Follower f " +
            "WHERE f.follower.id = :memberId OR f.member.id = :memberId")
    FollowCountDTO findFollowCountsByMemberId(@Param("memberId") Long memberId);
}

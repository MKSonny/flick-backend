package pro.Flick.repsository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Follower;
import pro.Flick.entity.Member;
import pro.Flick.member.FollowCountDTO;

public interface FollowRepository extends JpaRepository<Follower, Long> {


    @Query("SELECT new pro.Flick.member.FollowCountDTO(" +
            "SUM(CASE WHEN f.follower.id = :memberId THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN f.member.id = :memberId THEN 1 ELSE 0 END)) " +
            "FROM Follower f " +
            "WHERE f.follower.id = :memberId OR f.member.id = :memberId")
    FollowCountDTO findFollowCountsByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM Follower f where f.follower.id = :followerId and f.member.id = :memberId")
    void deleteFollowByFollowerIdAndMemberId(@Param("followerId") Long followerId, @Param("memberId") Long memberId);
}

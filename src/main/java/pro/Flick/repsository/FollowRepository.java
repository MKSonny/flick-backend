package pro.Flick.repsository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Follower;
import pro.Flick.member.dto.FollowCountDTO;

public interface FollowRepository extends JpaRepository<Follower, Long> {


    @Query("SELECT new pro.Flick.member.dto.FollowCountDTO(" +
            "SUM(CASE WHEN f.following.id = :memberId THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN f.followed.id = :memberId THEN 1 ELSE 0 END)) " +
            "FROM Follower f " +
            "WHERE f.following.id = :memberId OR f.followed.id = :memberId")
    FollowCountDTO findFollowCountsByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM Follower f where f.following.id = :followerId and f.followed.id = :memberId")
    void deleteFollowByFollowerIdAndMemberId(@Param("followerId") Long followerId, @Param("memberId") Long memberId);
}

package pro.Flick.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 올바르게 수정된 쿼리
    @Query("SELECT f.id, f.follower FROM Follower f WHERE f.member.id = :memberId")
    Page<Member> findFollowersByMemberId(Pageable pageable, @Param("memberId") Long memberId);

    Member findByUsername(String username);

    @Query("SELECT new pro.Flick.member.FollowerInfoDTO(f.id, f.follower) " +
            "FROM Follower f WHERE f.member.id = :memberId")
    Page<FollowerInfoDTO> findFollowersByMemberIdV2(Pageable pageable, @Param("memberId") Long memberId);

    //    @Query("select m.*, case when f.id is not null then true else false end" +
//            "from Member m" +
//            "left join Follower f" +
//            "on f.follower.id = m.id" +
//            "where m.id = :memberId")
//    Member findMemberInfoWithFollower();
    @Query("select new pro.Flick.member.MemberFollowStatusDTO(m, exists(select 1 from Follower f where f.follower.id = m.id and f.member.id = :userId)) " +
            "From Member m where m.id = :memberId")
    MemberFollowStatusDTO findMemberInfoWithFollowStatus(@Param("userId") Long userId, @Param("memberId") Long memberId);

    /**
     * 특정 멤버의 프로필 정보를 조회합니다.
     * @param profileId 프로필을 조회할 멤버의 ID
     * @param myId 현재 프로필을 보고 있는 (로그인한) 사용자의 ID
     * @return MemberProfileDto
     */
    @Query("SELECT new pro.Flick.member.ProfileInfoResponseDTOV2(" +
            "m, " +
            "(SELECT CASE WHEN COUNT(f1.id) > 0 THEN true ELSE false END " +
            " FROM Follower f1 WHERE f1.member.id = :profileId AND f1.follower.id = :myId), " + // viewer가 member를 팔로우하는지 여부
            "(SELECT COUNT(f2.id) FROM Follower f2 WHERE f2.member.id = :myId), " + // member의 팔로워 수
            "(SELECT COUNT(f3.id) FROM Follower f3 WHERE f3.follower.id = :myId)" +  // member가 팔로우하는 수
            ") " +
            "FROM Member m " +
            "WHERE m.id = :myId")
    ProfileInfoResponseDTOV2 findMemberProfile(@Param("myId") Long myId, @Param("profileId") Long profileId);
}

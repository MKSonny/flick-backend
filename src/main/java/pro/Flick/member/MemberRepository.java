package pro.Flick.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.entity.Member;
import pro.Flick.member.dto.FollowerInfoDTO;
import pro.Flick.member.dto.FollowerInfoDTOV2;
import pro.Flick.member.dto.MemberFollowStatusDTO;
import pro.Flick.member.dto.ProfileInfoResponseDTOV2;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 올바르게 수정된 쿼리
    @Query("SELECT f.id, f.following FROM Follower f WHERE f.followed.id = :memberId")
    Page<Member> findFollowersByMemberId(Pageable pageable, @Param("memberId") Long memberId);

    Member findByUsername(String username);

    List<Member> findMembersByUsername(String username);

    Member findMemberByEmailAndPassword(String email, String password);

    List<Member> findByIdIn(List<Long> ids);

    @Query("select m from Member m where m.id = :memberId")
    Member findMemberById(@Param("memberId") String memberId);

    Member findByEmail(String email);

    @Query("SELECT new pro.Flick.member.dto.FollowerInfoDTO(f.id, f.following) " +
            "FROM Follower f WHERE f.followed.id = :memberId")
    Page<FollowerInfoDTO> findFollowersByMemberIdV2(Pageable pageable, @Param("memberId") Long memberId);

    // 기존 V2 -> file 메커니즘 수정
//    @Query("SELECT m FROM Member m JOIN Follower f ON f.followed.id = m.id where f.followed.id = :memberId")
    @Query("SELECT new pro.Flick.member.dto.FollowerInfoDTOV2(m, f_check.id IS NOT NULL) FROM Member m JOIN Follower f ON m.id = f.following.id " +
            "LEFT JOIN Follower f_check ON m.id = f_check.followed.id and f_check.following.id = :memberId " +
            "where f.followed.id = :memberId")
    Page<FollowerInfoDTOV2> findFollowerByMemberIdV3(Pageable pageable, @Param("memberId") Long memberId);

    //    @Query("select m.*, case when f.id is not null then true else false end" +
//            "from Member m" +
//            "left join Follower f" +
//            "on f.followed.id = m.id" +
//            "where m.id = :memberId")
//    Member findMemberInfoWithFollower();
    @Query("select new pro.Flick.member.dto.MemberFollowStatusDTO(m, exists(select 1 from Follower f where f.following.id = m.id and f.followed.id = :userId)) " +
            "From Member m where m.id = :memberId")
    MemberFollowStatusDTO findMemberInfoWithFollowStatus(@Param("userId") Long userId, @Param("memberId") Long memberId);

    /**
     * 특정 멤버의 프로필 정보를 조회합니다.
     * @param profileId 프로필을 조회할 멤버의 ID
     * @param myId 현재 프로필을 보고 있는 (로그인한) 사용자의 ID
     * @return MemberProfileDto
     */
    @Query("SELECT new pro.Flick.member.dto.ProfileInfoResponseDTOV2(" +
            "m, " +
            "(SELECT CASE WHEN COUNT(f1.id) > 0 THEN true ELSE false END " +
            " FROM Follower f1 WHERE f1.followed.id = :profileId AND f1.following.id = :myId), " + // viewer가 member를 팔로우하는지 여부
            "(SELECT COUNT(f2.id) FROM Follower f2 WHERE f2.followed.id = :myId), " + // member의 팔로워 수
            "(SELECT COUNT(f3.id) FROM Follower f3 WHERE f3.following.id = :myId)" +  // member가 팔로우하는 수
            ") " +
            "FROM Member m " +
            "WHERE m.id = :myId")
    ProfileInfoResponseDTOV2 findMemberProfile(@Param("myId") Long myId, @Param("profileId") Long profileId);
}

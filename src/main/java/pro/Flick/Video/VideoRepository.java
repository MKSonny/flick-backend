package pro.Flick.Video;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.Flick.Video.dto.response.VideoSummaryResponse;
import pro.Flick.Video.trash.dto.VideoWithMemberAndFollowerInfoDtoV3;
import pro.Flick.entity.Video;

import java.util.Collection;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("select v from Video v join fetch v.member")
    List<Video> findVideosWithMember();

    @Query("select v from Video v join fetch v.member where v.member.id=:memberId")
    List<Video> findVideosByMemberIdWithMember(@Param("memberId") String memberId);

    @Query("select v from Video v join fetch v.member where v.member.id=:memberId")
    List<Video> findVideosByMemberIdWithMember(@Param("memberId") Long memberId);

    @Query("select v from Video v where v.member.id in :memberIds")
    List<Video> findVideoByMemberIds(Collection<Long> memberIds);

    @Query(value = "select v from Video v join fetch v.member m left join fetch m.file", countQuery = "select count(v.id) from Video v")
    Page<Video> findAllVideos(Pageable pageable);

    /*
        영상 정보 + 내가 이 사람을 팔로우 하고 있는지 + 좋아요 수 + 댓글 수
     */
    @Query(value = "select new pro.Flick.Video.trash.dto.VideoWithMemberAndFollowerInfoDtoV3(v, exists(select 1 from Follower f where f.follower.id = :memberId and f.member.id = v.member.id), exists(select 1 from Likes l where l.member.id = :memberId and l.video.id = v.id)) " +
            "from Video v join fetch v.member", countQuery = "select count(v.id) from Video v")
    Page<VideoWithMemberAndFollowerInfoDtoV3> findAllVideosV2(Pageable pageable, @Param("memberId") Long memberId);

    @Query(value = "select new pro.Flick.Video.dto.response.VideoSummaryResponse(v, exists(select 1 from Follower f where f.follower.id = :memberId and f.member.id = v.member.id), exists(select 1 from Likes l where l.member.id = :memberId and l.video.id = v.id)) " +
            "from Video v join fetch v.member", countQuery = "select count(v.id) from Video v")
    Page<VideoSummaryResponse> findAllVideosV3(Pageable pageable, @Param("memberId") Long memberId);

    @Query(value = "select v from Video v join fetch v.member where v.member.id = :memberId", countQuery = "select count(v.id) from Video v where v.member.id = :memberId")
    Page<Video> findAllVideosByMemberId(Pageable pageable, @Param("memberId") Long memberId);

    @Modifying
    @Query("update Video v set v.likesCount = v.likesCount + 1 where v.id = :id")
    int incrementLikesCount(Long id);

    @Modifying
    @Query("update Video v set v.likesCount = v.likesCount - 1 where v.id = :id")
    int decrementLikesCount(Long id);

    @Modifying
    @Query("update Video v set v.commentCount = v.commentCount + 1 where v.id = :id")
    int incrementCommentCount(Long id);

    @Modifying
    @Query("update Video v set v.commentCount = v.commentCount - 1 where v.id = :id")
    int decrementCommentCount(Long id);
}

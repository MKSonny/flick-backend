package pro.Flick.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.Flick.comment.dto.GetLiveCommentsResponse;
import pro.Flick.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT c from Comment c join fetch c.member where c.video.id = :videoId")
    List<Comment> findCommentByVideoId(@Param("videoId") Long videoId);

    @Modifying
    @Query("update Comment c set c.likesCount = c.likesCount + 1 where c.id = :id")
    int incrementLikesCount(Long id);

    @Modifying
    @Query("update Comment c set c.likesCount = c.likesCount - 1 where c.id = :id")
    int decrementLikesCount(Long id);

    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.member where c.video.id = :videoId", countQuery = "SELECT COUNT(c) FROM Comment c")
    Page<Comment> findAllComments(Pageable pageable, @Param("videoId") Long videoId);

    @Query(value = "SELECT new pro.Flick.comment.GetCommentsByVideoIdWithLikesInfoResponseDTO(c, CASE WHEN l.id IS NOT NULL THEN TRUE ELSE FALSE END)" +
            " FROM Comment c JOIN FETCH c.member LEFT JOIN Likes l ON c.id = l.comment.id and l.member.id = :memberId where c.video.id = :videoId")
    Page<GetCommentsByVideoIdWithLikesInfoResponseDTO> findAllCommentsWithLikesInfo(Pageable pageable, @Param("videoId") Long videoId, @Param("memberId") Long memberId);

    @Query("SELECT new pro.Flick.comment.GetReplysByParentIdWithLikesInfoResponseDTO(c, CASE WHEN l.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Comment c " +
            "LEFT JOIN Likes l ON l.comment = c AND l.member.id = :memberId " +
            "WHERE c.parent.id = :parentId")
    Page<GetReplysByParentIdWithLikesInfoResponseDTO> findAllReplysWithLikesInfo(Pageable pageable, @Param("memberId") Long memberId, @Param("parentId") Long parentId);

    /**
     * 중요! 상관 서브 쿼리 사용, 인덱스 설정 필요
     * 인덱스 설정 방법?
     */
    @Query("select new pro.Flick.comment.GetReplysByParentIdWithLikesInfoResponseDTO(c, exists(select 1 from Likes l where l.comment.id = c.id and l.member.id = :memberId)) " +
            "from Comment c where c.parent.id = :parentId")
    Page<GetReplysByParentIdWithLikesInfoResponseDTO> findAllReplysWithLikesInfoV2(Pageable pageable, @Param("memberId") Long memberId, @Param("parentId") Long parentId);


    /**
     * 중요! 인덱스 설정 필요
     * 인덱스 설정 방법?
     */
    @Query("select new pro.Flick.comment.GetCommentsByVideoIdWithLikesInfoResponseDTOV2(c, exists(select 1 from Likes l where l.comment.id = c.id and l.member.id = :memberId), (select count(c2) from Comment c2 where c2.parent.id = c.id)) " +
            "from Comment c where c.parent.id is null and c.video.id = :videoId")
    Page<GetCommentsByVideoIdWithLikesInfoResponseDTOV2> findAllCommentsWithLikesInfoV2(Pageable pageable, @Param("memberId") Long memberId, @Param("videoId") Long videoId);

    @Query("select new pro.Flick.comment.dto.GetLiveCommentsResponse(c.text, c.member.username) from Comment c where c.video.id = :videoId")
    Page<GetLiveCommentsResponse> findAllLiveCommentsByVideoId(Pageable pageable, @Param("videoId") Long videoId);
}

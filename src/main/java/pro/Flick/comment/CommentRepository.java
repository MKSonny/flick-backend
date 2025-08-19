package pro.Flick.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
}

package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Chat;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommentRepository {

    @Autowired
    EntityManager em;

    @Transactional
    public void addComment(Member member, Video video, String text) {
        em.persist(new Comment(member, video, text, LocalDateTime.now()));
    }

    /*
        오류 발생 7_24
        org.hibernate.query.sqm.PathElementException: Could not resolve attribute 'video_id' of 'pro.Flick.entity.Comment'

        Comment 엔티티 코드를 보면, video_id와 member_id는 모두 연관관계 (ManyToOne) 로 정의돼 있고, 해당 필드는 Video와 Member 객체 자체입니다.
        즉, 단순한 Long 타입이 아니라 객체 참조이죠.

        Comment 엔티티에는 videoId 같은 필드가 없고 대신:
        video 필드가 있어서, JPA에서 쿼리를 작성할 때도 c.video를 기준으로 필터링해야 합니다.


        오류 나는 잘못된 쿼리
        @Query("SELECT c FROM Comment c WHERE c.video_id = :videoId")  // video_id는
     */
    public List<Comment> getCommentsByVideoId(String videoId) {
        return em.createQuery("select c from Comment c where c.video.id=:videoId", Comment.class)
                .setParameter("videoId", videoId)
                .getResultList();
    }
}

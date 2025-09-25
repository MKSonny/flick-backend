package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Likes;
import pro.Flick.member.entity.Member;
import pro.Flick.entity.Video;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LikesJpaRepository {
    @Autowired
    EntityManager em;

    @Transactional
    public Likes addLike(Member member, Video video) {
        Likes likes = new Likes(member, video, LocalDateTime.now());
        em.persist(likes);
        return likes;
    }

    public Long findLikesCountByMemberId(String memberId) {
        return em.createQuery("select count(l) from Likes l where l.video.member.id =:memberId", Long.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

    public List<Likes> findLikesByMemberId(String memberId) {
        return em.createQuery("select l from Likes l where l.member.id=:memberId", Likes.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    // 활동
    // 내 동영상들에서 내가 받은 좋아요
    // 페이징 기능 추가 필요
    public List<Likes> findLikesOnMyVideo(String memberId) {
        return em.createQuery("select l from Likes l join fetch l.video v join fetch l.member where v.member.id=:memberId "+
                        "order by l.createdAt desc", Likes.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Transactional
    public void deleteLikes(String memberId, String videoId) {
        em.createQuery("delete from Likes l where l.member.id=:memberId and l.video.id=:videoId")
                .setParameter("memberId", memberId)
                .setParameter("videoId", videoId)
                .executeUpdate();
    }
}

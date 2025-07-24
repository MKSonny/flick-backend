package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Likes;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;

import java.time.LocalDateTime;

@Repository
public class LikesRepository {
    @Autowired
    EntityManager em;

    @Transactional
    public void addLike(Member member, Video video) {
        em.persist(new Likes(member, video, LocalDateTime.now()));
    }
}

package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;

import java.util.List;

@Repository
public class FileRepository {
    @Autowired
    EntityManager em;

    /*
    @Transactional
    public Member save(String username, String email, String password) {
        Member member = new Member(username, email, password);
        em.persist(member);
        return member;
    }
     */

    @Transactional
    public Video saveVideo(String videoTitle, String uri, Member member) {
        Video video = new Video(videoTitle, uri, member);
        em.persist(video);
        return video;
    }

    public List<Video> getAllVideos() {
        List<Video> selectVFormVideoV = em.createQuery("select v from Video v", Video.class).getResultList();
        return selectVFormVideoV;
    }
}

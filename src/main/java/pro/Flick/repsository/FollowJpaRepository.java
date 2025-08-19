package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Follower;
import pro.Flick.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FollowJpaRepository {
    @Autowired
    EntityManager em;

    @Transactional
    public void memberAFollowsMemberB(Member memberA, Member memberB) {
        // 주의! followerId가 팔로우한 사람이고 member가 팔로우 당한 사람이다
        em.persist(new Follower(memberA, memberB, LocalDateTime.now()));
    }

    // 기존 follower.id가 잘못 설정되었음 -> follower.id가 내 id임 내가 팔로우한다는 의미
    // f.member.id는 팔로우 당한 사람의 id
    // 내가 팔로워인 모든 곳
    public List<Follower> getFollowingByMemberId(String id) {
        return em.createQuery("select f from Follower f where f.follower.id=:id", Follower.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Transactional
    public void deleteFollower(String id, String followerId) {
        em.createQuery("delete from Follower f where f.member.id=:id and f.follower.id=:followerId")
                .setParameter("id", id)
                .setParameter("followerId", followerId)
                .executeUpdate();

    }

    public List<Follower> getFollowers(String id) {
        return em.createQuery("select f from Follower f where f.follower.id=:id", Follower.class)
                .setParameter("id", id)
                .getResultList();
    }

    // 멤버가 하나고 팔로워가 여러명
    // 팀이 하나고 티원이 여러명
    public List<Follower> getFollowersFetch(String memberId) {
        return em.createQuery("select f from Follower f join fetch f.member where f.member.id =:memberId", Follower.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}

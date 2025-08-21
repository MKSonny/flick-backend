package pro.Flick.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;

import java.util.List;

@Repository
public class MemberJpaRepository {

    @Autowired
    EntityManager em;

    @Transactional
    public Member save(String username, String email, String password) {

        Member member = Member.builder()
                        .username(username)
                                .email(email)
                                        .password(password).build();
        em.persist(member);
        return member;
    }

    @Transactional
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findMemberById(String id) {
        String jpql = "select m from Member m where m.id=:id";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class)
                .setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Member findMemberByEmail(String email) {
        String jpql = "select m from Member m where m.email=:email";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class)
                .setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Member findMember(String email, String password) {
        String jpql = "select m from Member m where m.email=:email and m.password=:password";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class)
                .setParameter("email", email)
                .setParameter("password", password);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // 대소문자 구분하지 않기 위해 lower(m.username), username.toLowerCase() 추가
    public List<Member> findMemberByUsername(String username) {
        return em.createQuery("select m from Member m where lower(m.username) like :username", Member.class)
                .setParameter("username", "%" + username.toLowerCase() + "%")
                .getResultList();
    }

    public Member findMemberByUsernameSingle(String username) {
        return em.createQuery("select m from Member m where lower(m.username) like :username", Member.class)
                .setParameter("username", "%" + username.toLowerCase() + "%")
                .getSingleResult();

    }

    public List<Member> findMemberByIds(List<Long> ids) {
        return em.createQuery("select m from Member m where m.id in :ids", Member.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Transactional
    public void updateProfileImage(String memberId, String imageUri) {
        em.createQuery("update Member m set m.profileImageUri = :imageUri where m.id = :memberId")
                .setParameter("imageUri", imageUri)
                .setParameter("memberId", memberId)
                .executeUpdate();
    }
}

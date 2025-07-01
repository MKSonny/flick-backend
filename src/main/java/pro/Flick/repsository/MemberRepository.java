package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;

@Repository
public class MemberRepository {

    @Autowired
    EntityManager em;

    @Transactional
    public Member save(String username, String email, String password) {
        Member member = new Member(username, email, password);
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
}

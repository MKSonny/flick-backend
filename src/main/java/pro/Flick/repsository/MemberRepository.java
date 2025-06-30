package pro.Flick.repsository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;

@Repository
public class MemberRepository {

    @Autowired
    EntityManager em;

    @Transactional
    public void save(String email) {
        Member member = new Member(email);
        em.persist(member);
    }
}

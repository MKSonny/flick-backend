package pro.Flick;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class FlickApplicationTests {

    @Autowired
    EntityManager em;



    @Test
    void contextLoads() {

    }

    @Test
    @Transactional
    void memberEntityTest() {
//		Member member = new Member("hello", "hello@email.com");
//		em.persist(member);
    }
}

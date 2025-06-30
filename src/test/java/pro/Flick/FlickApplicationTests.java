package pro.Flick;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;

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
		Member member = new Member("hello", "hello@email.com");
		em.persist(member);
	}
}

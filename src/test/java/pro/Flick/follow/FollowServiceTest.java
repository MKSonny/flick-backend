package pro.Flick.follow;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.Flick.entity.Member;
import pro.Flick.member.MemberRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FollowServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    FollowService followService;

    @Autowired
    MemberRepository memberRepository;

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        // 각 테스트가 시작되기 전에 SessionFactory에서 Statistics 객체를 가져오고 초기화합니다.
        statistics = em.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
    }


    @Test
    void addFollowAndDeleteTest() {
        Member memberA = memberRepository.save(new Member("memberA", "emailA", "123"));
        Member memberB = memberRepository.save(new Member("memberB", "emailB", "123"));

        statistics.clear();

        followService.memberAFollowsMemberB(memberA, memberB);
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);

        statistics.clear();

        followService.deleteFollower(memberA.getId(), memberB.getId());
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }
}
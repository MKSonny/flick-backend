//package pro.Flick.follow;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import pro.Flick.entity.Member;
//import pro.Flick.entity.QFollower;
//
//public class FollowRepositoryImpl implements FollowerRepositoryCustom{
//
//    private JPAQueryFactory queryFactory;
//
//    public FollowRepositoryImpl(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//    @Override
//    public Page<Member> findFollowersByMemberId(Pageable pageable, Long memberId) {
//
//    }
//}

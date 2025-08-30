package pro.Flick.follow;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.entity.Member;
import pro.Flick.entity.QFollower;
import pro.Flick.member.dto.FollowerInfoDTOV2;

import static pro.Flick.entity.QFollower.follower;

//public class FollowRepositoryImpl implements FollowerRepositoryCustom{
//
//    private JPAQueryFactory queryFactory;
//
//    public FollowRepositoryImpl(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//}

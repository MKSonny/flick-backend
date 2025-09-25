package pro.Flick.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import pro.Flick.entity.QFollower;
import pro.Flick.member.dto.ProfileInfoResponseDTOV2;

import static com.querydsl.jpa.JPAExpressions.*;
import static pro.Flick.member.entity.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ProfileInfoResponseDTOV2 QfindMemberProfile(Long myId, Long profileId) {

        QFollower f1 = new QFollower("f1");
        QFollower f2 = new QFollower("f2");
        QFollower f3 = new QFollower("f3");

        return queryFactory
                .select(Projections.fields(ProfileInfoResponseDTOV2.class, member,
                        selectOne()
                                .from(f1)
                                .where(f1.following.id.eq(profileId).and(f1.followed.id.eq(myId)))
                                .exists(),
                        select(f2.id.count())
                                .from(f2)
                                .where(f2.followed.id.eq(myId)),
                        select(f3.id.count())
                                .from(f3)
                                .where(f3.following.id.eq(myId))
                        )
                )
                .from(member)
                .where(member.id.eq(myId))
                .fetchOne();
    }
}

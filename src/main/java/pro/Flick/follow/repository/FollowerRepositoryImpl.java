package pro.Flick.follow.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import pro.Flick.entity.QFollower;

import static pro.Flick.entity.QFollower.follower;

@RequiredArgsConstructor
public class FollowerRepositoryImpl implements FollowerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean QfindAmIFollowing(Long myId, Long profileUserId) {
        Integer i = queryFactory
                .selectOne()
                .from(follower)
                .where(follower.followed.id.eq(myId), follower.following.id.eq(profileUserId))
                .fetchFirst();

        return i != null;
    }
}

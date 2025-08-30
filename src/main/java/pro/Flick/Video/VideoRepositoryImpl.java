package pro.Flick.Video;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.Video.dto.response.VideoSummaryResponse;
import pro.Flick.entity.*;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static pro.Flick.entity.QFollower.follower;
import static pro.Flick.entity.QLikes.likes;
import static pro.Flick.entity.QMember.member;
import static pro.Flick.entity.QVideo.video;

@Slf4j
public class VideoRepositoryImpl implements VideoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public VideoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Video> QfindVideosByMemberIdWithMember(Long memberId) {

        log.info("QfindVideosByMemberIdWithMember worked");

        return queryFactory
                .select(video)
                .join(video.member, member).fetchJoin()
                .fetch();
    }

    @Override
    public Page<VideoSummaryResponse> QfindAllVideosV3(Pageable pageable, Long memberId) {

        List<VideoSummaryResponse> content = queryFactory
                .select(Projections.constructor(
                        VideoSummaryResponse.class,
                        video,
                        selectOne()
                                .from(follower)
                                .where(follower.followed.id.eq(memberId), follower.followed.id.eq(video.member.id))
                                .exists()
                        ,
                        selectOne()
                                .from(likes)
                                .where(likes.member.id.eq(memberId), likes.video.id.eq(video.id))
                                .exists()
                ))
                .from(video)
                .join(video.member, member).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchJoin()
                .fetch();

        Long total = queryFactory
                .select(video.count())
                .from(video)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}

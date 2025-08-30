package pro.Flick.Video;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import pro.Flick.entity.QMember;
import pro.Flick.entity.QVideo;
import pro.Flick.entity.Video;

import java.util.List;

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
}

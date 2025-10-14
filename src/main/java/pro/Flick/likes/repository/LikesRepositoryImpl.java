package pro.Flick.likes.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.Video.dto.response.VideoSummaryResponse;
import pro.Flick.entity.QLikes;
import pro.Flick.entity.QVideo;
import pro.Flick.member.dto.LikedVideoResponseDTO;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.selectOne;
import static pro.Flick.entity.QFollower.follower;
import static pro.Flick.entity.QLikes.likes;
import static pro.Flick.entity.QVideo.video;
import static pro.Flick.member.entity.QMember.member;

@RequiredArgsConstructor
public class LikesRepositoryImpl implements LikesRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<LikedVideoResponseDTO> QfindLikedVideosByMemberId(Pageable pageable, Long memberId) {
        List<LikedVideoResponseDTO> content = queryFactory
                .select(Projections.constructor(LikedVideoResponseDTO.class,
                        video.id,
                        video.thumbnailStoreFileName
                ))
                .from(likes)
                .join(likes.video, video)
                .where(likes.member.id.eq(memberId))
                .fetch();

        Long total = queryFactory
                .select(likes.count())
                .from(likes)
                .join(likes.video, video)
                .where(likes.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<VideoSummaryResponse> QfindLikedVideosByMemberIdV2(Pageable pageable, Long memberId) {
        List<VideoSummaryResponse> content = queryFactory
                .select(Projections.constructor(
                        VideoSummaryResponse.class,
                        video,
                        selectOne()
                                .from(follower)
                                .where(follower.followed.id.eq(memberId), follower.following.id.eq(video.member.id))
                                .exists()
                        ,
                        selectOne()
                                .from(likes)
                                .where(likes.member.id.eq(memberId), likes.video.id.eq(video.id))
                                .exists()
                ))
                .from(likes)
                .join(likes.video, video)
                .join(video.member, member).fetchJoin()
                .where(likes.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchJoin()
                .fetch();

        Long total = queryFactory
                .select(likes.count())
                .from(likes)
                .where(likes.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}

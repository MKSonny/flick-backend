package pro.Flick.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pro.Flick.comment.dto.response.CommentDetailResponseDTO;
import pro.Flick.comment.dto.response.CommentReplyDetailResponseDTO;
import pro.Flick.comment.dto.response.LiveCommentsResponseDTO;
import pro.Flick.entity.Comment;
import pro.Flick.entity.QComment;
import pro.Flick.entity.QLikes;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static pro.Flick.entity.QComment.comment;
import static pro.Flick.entity.QLikes.likes;
import static pro.Flick.member.entity.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentDetailResponseDTO> QfindAllCommentsWithLikesInfoV3(Pageable pageable, Long memberId, Long videoId) {

        QComment c2 = new QComment("c2");

        List<CommentDetailResponseDTO> content = queryFactory.select(
                        Projections.constructor(CommentDetailResponseDTO.class,
                                comment,
                                selectOne()
                                        .from(likes)
                                        .where(likes.comment.id.eq(comment.id), likes.member.id.eq(memberId))
                                        .exists(),
                                select(c2.count())
                                        .from(c2)
                                        .where(c2.parent.id.eq(comment.id))
                        ))
                .from(comment)
                .where(comment.parent.id.isNull(), comment.video.id.eq(videoId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .fetchOne();


        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<CommentReplyDetailResponseDTO> QfindAllReplysWithLikesInfoV3(Pageable pageable, Long memberId, Long parentId) {

        List<CommentReplyDetailResponseDTO> content = queryFactory
                .select(Projections.constructor(CommentReplyDetailResponseDTO.class,
                        comment,
                        selectOne()
                                .from(likes)
                                .where(likes.comment.id.eq(comment.id), likes.member.id.eq(memberId))
                                .exists()
                ))
                .from(comment)
                .where(comment.parent.id.eq(parentId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdAt.asc())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Comment> QfindCommentByVideoId(Long videoId) {
        return queryFactory
                .select(comment)
                .from(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.video.id.eq(videoId))
                .fetch();
    }

    @Override
    public Page<LiveCommentsResponseDTO> QfindAllLiveCommentsByVideoId(Pageable pageable, Long videoId) {

        List<LiveCommentsResponseDTO> content = queryFactory.select(
                        Projections.constructor(LiveCommentsResponseDTO.class,
                                comment.text,
                                comment.member.username
                        ))
                .from(comment)
                .where(comment.video.id.eq(videoId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.video.id.eq(videoId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}

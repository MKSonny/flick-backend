package pro.Flick.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.comment.dto.response.CommentDetailResponseDTO;

//@RequiredArgsConstructor
//public class CommentRepositoryImpl implements CommentRepositoryCustom {
//
//    private JPAQueryFactory queryFactory;
//
//    @Override
//    public Page<CommentDetailResponseDTO> findAllCommentsWithLikesInfoV3(Pageable pageable, Long memberId, Long videoId) {
//
//        queryFactory.select(
//
//        )
//
//    }
//}

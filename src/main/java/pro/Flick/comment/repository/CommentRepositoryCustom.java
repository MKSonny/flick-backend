package pro.Flick.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.Flick.comment.dto.response.CommentDetailResponseDTO;

public interface CommentRepositoryCustom {
    Page<CommentDetailResponseDTO> findAllCommentsWithLikesInfoV3(Pageable pageable, Long memberId, Long videoId);
}

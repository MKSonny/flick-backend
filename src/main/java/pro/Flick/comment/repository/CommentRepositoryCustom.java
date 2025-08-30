package pro.Flick.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pro.Flick.comment.dto.response.CommentDetailResponseDTO;
import pro.Flick.comment.dto.response.CommentReplyDetailResponseDTO;
import pro.Flick.comment.dto.response.LiveCommentsResponseDTO;
import pro.Flick.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    Page<CommentDetailResponseDTO> QfindAllCommentsWithLikesInfoV3(Pageable pageable, Long memberId, Long videoId);
    Page<CommentReplyDetailResponseDTO> QfindAllReplysWithLikesInfoV3(Pageable pageable, Long memberId, Long parentId);
    List<Comment> QfindCommentByVideoId(Long videoId);
    Page<LiveCommentsResponseDTO> QfindAllLiveCommentsByVideoId(Pageable pageable, Long videoId);
}

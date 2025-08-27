package pro.Flick.comment;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.Flick.Video.VideoJpaRepository;
import pro.Flick.Video.VideoRepository;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Likes;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.LikesRepository;
import pro.Flick.member.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public List<GetCommentsByVideoIdResponseDTO> findCommentByVideoId(Long videoId) {
        List<Comment> comments = commentRepository.findCommentByVideoId(videoId);
        return comments.stream().map(GetCommentsByVideoIdResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public Page<GetCommentsByVideoIdResponseDTO> findAllComments(Pageable pageable, Long videoId) {
        Page<Comment> comments = commentRepository.findAllComments(pageable, videoId);



        return comments.map(GetCommentsByVideoIdResponseDTO::new);
    }

    @Transactional
    public Page<GetCommentsByVideoIdWithLikesInfoResponseDTO> findAllCommentsWithLikesInfo(Pageable pageable, Long videoId, Long memberId) {
        return commentRepository.findAllCommentsWithLikesInfo(pageable, videoId, memberId);
    }

    public Page<GetCommentsByVideoIdWithLikesInfoResponseDTOV2> findAllCommentsWithLikesInfoV2(Pageable pageable, Long videoId, Long memberId) {
        return commentRepository.findAllCommentsWithLikesInfoV2(pageable, memberId, videoId);
    }

    @Transactional
    public void addCommentV1(Long memberId, Long videoId, String text) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Video video = videoJpaRepository.findVideoById(String.valueOf(videoId));
        Comment comment = new Comment(member, video, text, LocalDateTime.now());
        commentRepository.save(comment);

    }

    @Transactional
    public Comment addCommentV2(Long memberId, Long videoId, String text) {

        try {
            // SELECT 없이 프록시로 관계 설정
            Member memberReference = memberRepository.getReferenceById(memberId);
            Video videoReference = videoRepository.getReferenceById(videoId);

            Comment comment = new Comment(memberReference, videoReference, text, LocalDateTime.now());

            videoRepository.incrementCommentCount(videoId);
            commentRepository.save(comment);
            return comment;
        } catch (EntityNotFoundException e) {
            // 존재하지 않는 ID 사용 시 발생하는 예외를 잡아서 처리한다
            log.error("댓글 생성 실패: 존재하지 않는 회원(ID: {}) 또는 비디오(ID: {})입니다.", memberId, videoId);
            // 비즈니스에 맞는 구체적인 예외로 변환하여 던지는 코드 구현 필요
            return null;
        }
    }


    /**
     * version 사용(낙관적 락)
     * @param memberId
     * @param commentId
     */
    @Transactional
    public void addCommentLikes(Long memberId, Long commentId) {

        Member memberRef = memberRepository.getReferenceById(memberId);

        Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
        comment.incrementCommentLikesCount();
        likesRepository.save(new Likes(memberRef, comment, LocalDateTime.now()));
        log.info("addCommentLikes end");
    }



    /**
     * 낙관적 락 사용
     * @param memberId
     * @param commentId
     */
    @Transactional
    public void removeCommentLikes(Long memberId, Long commentId) {
        likesRepository.deleteLikesByMemberIdAndCommentId(memberId, commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.decrementLikesCount();
    }


    @Transactional
    public GetReplysByParentIdWithLikesInfoResponseDTO addReply(Long userId, Long videoId, Long parentId, String text) {
        Member member = memberRepository.findById(userId).orElseThrow();

        Video video = videoRepository.getReferenceById(videoId);

        Comment parent = commentRepository.getReferenceById(parentId);


        Comment savedReply = commentRepository.save(new Comment(member, video, text, parent, LocalDateTime.now()));
        return new GetReplysByParentIdWithLikesInfoResponseDTO(savedReply, false);
    }

    public Page<GetReplysByParentIdWithLikesInfoResponseDTO> getRepliesByParentId(Pageable pageable, Long parentId, Long userId) {
//        return commentRepository.findAllReplysWithLikesInfo(pageable, userId, parentId);
        return commentRepository.findAllReplysWithLikesInfoV2(pageable, userId, parentId);
    }

    @Transactional
    public CommentLiveController.ChatMessageResponse addLiveComment(Long memberId, String content, Long videoId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Video video = videoRepository.getReferenceById(videoId);
        Comment save = commentRepository.save(new Comment(member, video, content, LocalDateTime.now()));
        return new CommentLiveController.ChatMessageResponse(save, member, videoId);
    }
}

package pro.Flick.likes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.Flick.Video.VideoRepository;
import pro.Flick.comment.CommentRepository;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Likes;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.LikesRepository;
import pro.Flick.member.MemberRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesService {

    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    private final LikesRepository likesRepository;


    @Transactional
    public void removeVideoLikes(Long memberId, Long videoId) {
        likesRepository.deleteLikesByMemberIdAndVideoId(memberId, videoId);
        videoRepository.decrementLikesCount(videoId);
    }

    @Transactional
    public void removeCommentLikes(Long memberId, Long commentId) {
        likesRepository.deleteLikesByMemberIdAndCommentId(memberId, commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.decrementLikesCount();
    }

    @Transactional
    public void addCommentLikes(Long memberId, Long commentId) {
        Member memberRef = memberRepository.getReferenceById(memberId);

        Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
        comment.incrementCommentLikesCount();
        likesRepository.save(new Likes(memberRef, comment, LocalDateTime.now()));
    }

    @Transactional
    public void addCommentLikesModifyingV2(Long memberId, Long commentId) {
        log.info("addCommentLikes start");

        Member memberRef = memberRepository.getReferenceById(memberId);
        Comment comment = commentRepository.getReferenceById(commentId);

        commentRepository.incrementLikesCount(commentId);
        likesRepository.save(new Likes(memberRef, comment, LocalDateTime.now()));
        log.info("addCommentLikes end");
    }

    @Transactional
    public void removeCommentLikesModifyingV2(Long memberId, Long commentId) {
        likesRepository.deleteLikesByMemberIdAndCommentId(memberId, commentId);
        commentRepository.decrementLikesCount(commentId);
    }

    @Transactional
    public void addLikes(String userId, String videoId) {

        Member member = memberRepository.getReferenceById(Long.valueOf(userId));
        Video video = videoRepository.getReferenceById(Long.valueOf(videoId));

        videoRepository.incrementLikesCount(Long.valueOf(videoId));

        likesRepository.save(new Likes(member, video, LocalDateTime.now()));
    }
}

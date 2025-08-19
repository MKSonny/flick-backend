package pro.Flick.comment;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.Flick.Video.VideoJpaRepository;
import pro.Flick.Video.VideoRepository;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.CommentRepository;
import pro.Flick.repsository.MemberJpaRepository;
import pro.Flick.repsository.MemberRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final VideoJpaRepository videoJpaRepository;


    @Transactional
    public void addCommentV1(Long memberId, Long videoId, String text) {
        Member member = memberJpaRepository.findMemberById(String.valueOf(memberId));
        Video video = videoJpaRepository.findVideoById(String.valueOf(videoId));
        Comment comment = new Comment(member, video, text, LocalDateTime.now());
        commentRepository.save(comment);

    }

    @Transactional
    public void addCommentV2(Long memberId, Long videoId, String text) {

        try {
            // SELECT 없이 프록시로 관계 설정
            Member memberReference = memberRepository.getReferenceById(memberId);
            Video videoReference = videoRepository.getReferenceById(videoId);

            Comment comment = new Comment(memberReference, videoReference, text, LocalDateTime.now());

            commentRepository.save(comment);

        } catch (EntityNotFoundException e) {
            // 존재하지 않는 ID 사용 시 발생하는 예외를 잡아서 처리한다
            log.error("댓글 생성 실패: 존재하지 않는 회원(ID: {}) 또는 비디오(ID: {})입니다.", memberId, videoId);
            // 비즈니스에 맞는 구체적인 예외로 변환하여 던지는 코드 구현 필요
        }
    }
}

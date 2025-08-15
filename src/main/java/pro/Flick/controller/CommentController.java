package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.CommentRepository;
import pro.Flick.Video.VideoJpaRepository;
import pro.Flick.repsository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final VideoJpaRepository fileRepository;


    @GetMapping("/comments/{videoId}")
    public List<CommentDto> getCommentsByVideoId(@PathVariable String videoId) {
        List<Comment> comments = commentRepository.getCommentsByVideoId(videoId);
        List<CommentDto> commentDtos = new ArrayList<>();

        for (Comment comment : comments) {
            commentDtos.add(new CommentDto(comment.getText(), comment.getCreatedAt(), comment.getMember()));
        }

        return commentDtos;
    }

    @PostMapping("/comments")
    public void addComment(@RequestBody CommentAddDto requestDto) {
        log.info("requestDto={}", requestDto);

        Member findMember = memberRepository.findMemberById(requestDto.getUserId());
        Video findVideo = fileRepository.findVideoById(requestDto.getVideoId());

        commentRepository.addComment(findMember, findVideo, requestDto.getText());
    }

    // 활동
    // 내가 올린 동영상들을 찾음 -> 그 동영상의 댓글들을 가져옴
    @GetMapping("/my_video_comments/{memberId}")
    public List<CommentDto> getCommentsByMemberId(@PathVariable String memberId) {
        Member findMember = memberRepository.findMemberById(memberId);
        List<Comment> comments = commentRepository.getAllMyCommentsOnMyVideo(findMember.getId());
        List<CommentDto> commentDtos = new ArrayList<>();

        // 내가 올린 영상들과 그 댓글들을 조인해야 함
        for (Comment comment : comments) {
            commentDtos.add(new CommentDto(comment.getText(), comment.getCreatedAt(), comment.getMember()));
        }
        return commentDtos;
    }

    @Data
    static class CommentAddDto {
        private String userId;
        private String videoId;
        private String text;
        private String video_user_id;

        public CommentAddDto(String userId, String videoId, String text, String video_user_id) {
            this.userId = userId;
            this.videoId = videoId;
            this.text = text;
            this.video_user_id = video_user_id;
        }
    }

    @Data
    static class CommentDto {
        private String text;
        private LocalDateTime createdAt;
        private GetMemberByIdResponseDto user;

        public CommentDto(String text, LocalDateTime createdAt, Member member) {
            this.text = text;
            this.createdAt = createdAt;
            this.user = new GetMemberByIdResponseDto(member);
        }
    }
}

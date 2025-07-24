package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.entity.Comment;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.CommentRepository;
import pro.Flick.repsository.FileRepository;
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
    private final FileRepository fileRepository;


    @GetMapping("/comments/{videoId}")
    public List<CommentDto> getCommentsByVideoId(@PathVariable String videoId) {
        List<Comment> comments = commentRepository.getCommentsByVideoId(videoId);
        List<CommentDto> commentDtos = new ArrayList<>();

        for (Comment comment : comments) {
            commentDtos.add(new CommentDto(comment.getMember().getId(), comment.getText(), comment.getCreatedAt()));
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

    @Data
    static class CommentAddDto {
        private String userId;
        private String videoId;
        private String text;

        public CommentAddDto(String userId, String videoId, String text) {
            this.userId = userId;
            this.videoId = videoId;
            this.text = text;
        }
    }

    @Data
    static class CommentDto {
        private Long member_id;
        private String text;
        private LocalDateTime createdAt;

        public CommentDto(Long member_id, String text, LocalDateTime createdAt) {
            this.member_id = member_id;
            this.text = text;
            this.createdAt = createdAt;
        }
    }
}

package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.Video.service.VideoService;
import pro.Flick.comment.CommentLikesAddDTO;
import pro.Flick.comment.CommentService;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Likes;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.Video.VideoJpaRepository;
import pro.Flick.repsository.LikesJpaRepository;
import pro.Flick.repsository.MemberJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesJpaRepository likesJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final VideoService videoService;
    private final CommentService commentService;

//    @PostMapping("/likes")
    public void addLikes(@RequestBody LikesRequestDto likesRequestDto) {
//        videoService.addLikes(likesRequestDto.getUserId(), likesRequestDto.getVideoId());
        Member findMember = memberJpaRepository.findMemberById(likesRequestDto.getUserId());
        Video findVideo = videoJpaRepository.findVideoById(likesRequestDto.getVideoId());
        Likes likes = likesJpaRepository.addLike(findMember, findVideo);

//        videoService.addLikes(findVideo);
    }

    @PostMapping("/likes")
    public void addLikesV2(@RequestBody LikesRequestDto likesRequestDto) {
        videoService.addLikes(likesRequestDto.getUserId(), likesRequestDto.getVideoId());
    }

    @PostMapping("likes/comment")
    public void addCommentLikes(@RequestBody CommentLikesAddDTO requestDto) {
        commentService.addCommentLikes(requestDto.getUserId(), requestDto.getCommentId());
    }

    // 매번 영상을 불러올때마다 스프링에 데이터를 가져오는 것은 매우 비효율적
    // 그래서 아래 코드 작성
    // Likes 테이블에서 특정 member_id를 기준으로 해당 유저가 누른 좋아요들을 조회
    @GetMapping("/likes/{memberId}")
    public List<GetLikesByMemberIdResponseDto> getLikesByMemberId(@PathVariable String memberId) {
        List<Likes> likesByMemberId = likesJpaRepository.findLikesByMemberId(memberId);
        List<GetLikesByMemberIdResponseDto> GetLikesByMemberIdResponseDto = new ArrayList<>();

        for (Likes likes : likesByMemberId) {
            GetLikesByMemberIdResponseDto.add(new GetLikesByMemberIdResponseDto(likes.getId(), likes.getMember().getId(), likes.getVideo().getId(), likes.getCreatedAt()));
        }

        return GetLikesByMemberIdResponseDto;
    }

    // 내가 받은 좋아요
    @GetMapping("my_vid/likes/{memberId}")
    public Long getRespondLikesByMEmberId(@PathVariable String memberId) {
        Long c = likesJpaRepository.findLikesCountByMemberId(memberId);
        log.info("adfsfafsf={}", c);
        return c;
    }

    // 활동 페이지
    // 내가 올린 영상에 좋아요를 누른 멤버들을 가져옴
    @GetMapping("/likes/my_video/{memberId}")
    public List<Temp> getLikesOnMyVideo(@PathVariable String memberId) {
        Member findMember = memberJpaRepository.findMemberById(memberId);
        List<Likes> likes = likesJpaRepository.findLikesOnMyVideo(memberId);
        List<Temp> dtoList = new ArrayList<>();


        for (Likes like : likes) {
            dtoList.add(new Temp(like.getCreatedAt(), like.getMember()));
        }

        return dtoList;
    }


    @DeleteMapping("/likes")
    public void deleteLikes(@RequestParam Long userId, @RequestParam Long videoId) {
        videoService.removeLikes(userId, videoId);
    }

    @DeleteMapping("/likes/comment")
    public void deleteCommentLikes(@RequestParam Long userId, @RequestParam Long commentId) {
        commentService.removeCommentLikes(userId, commentId);
    }

    @Data
    static class Temp {
        private GetMemberByIdResponseDto user;
        private LocalDateTime createdAt;

        public Temp(LocalDateTime createdAt, Member member) {
            this.createdAt = createdAt;
            this.user = new GetMemberByIdResponseDto(member);
        }
    }

    @Data
    static class GetLikesByMemberIdResponseDto {
        private Long id;
        private Long memberId;
        private Long video_id;
        private LocalDateTime createdAt;

        public GetLikesByMemberIdResponseDto(Long id, Long memberId, Long video_id, LocalDateTime createdAt) {
            this.id = id;
            this.memberId = memberId;
            this.video_id = video_id;
            this.createdAt = createdAt;
        }
    }

    @Data
    static class LikesRequestDto {
        private String userId;
        private String videoId;
        private String videoUserId;

        public LikesRequestDto(String userId, String videoId, String videoUserId) {
            this.userId = userId;
            this.videoId = videoId;
            this.videoUserId = videoUserId;
        }
    }
}

package pro.Flick.likes;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.comment.trash.dto.CommentLikesAddDTO;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Likes;
import pro.Flick.entity.Member;
import pro.Flick.member.MemberRepository;
import pro.Flick.repsository.LikesJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesControllerV2 {

    private final LikesJpaRepository likesJpaRepository;
    private final MemberRepository memberRepository;
    private final LikesService likesService;


    @PostMapping
    public void addLikesV2(@RequestBody LikesRequestDTO likesRequestDTO) {
        likesService.addLikes(likesRequestDTO.getUserId(), likesRequestDTO.getVideoId());
    }

    @PostMapping("/comment")
    public void addCommentLikes(@RequestBody CommentLikesAddDTO requestDto) {
        likesService.addCommentLikesModifyingV2(requestDto.getUserId(), requestDto.getCommentId());
    }

    // 매번 영상을 불러올때마다 스프링에 데이터를 가져오는 것은 매우 비효율적
    // 그래서 아래 코드 작성
    // Likes 테이블에서 특정 member_id를 기준으로 해당 유저가 누른 좋아요들을 조회
    @GetMapping("/{memberId}")
    public List<GetLikesByMemberIdResponseDTO> getLikesByMemberId(@PathVariable String memberId) {
        List<Likes> likesByMemberId = likesJpaRepository.findLikesByMemberId(memberId);
        List<GetLikesByMemberIdResponseDTO> GetLikesByMemberIdResponseDTP = new ArrayList<>();

        for (Likes likes : likesByMemberId) {
            GetLikesByMemberIdResponseDTP.add(new GetLikesByMemberIdResponseDTO(likes.getId(), likes.getMember().getId(), likes.getVideo().getId(), likes.getCreatedAt()));
        }

        return GetLikesByMemberIdResponseDTP;
    }

    // 내가 받은 좋아요
//    @GetMapping("my_vid/likes/{memberId}")
    public Long getRespondLikesByMEmberId(@PathVariable String memberId) {
        Long c = likesJpaRepository.findLikesCountByMemberId(memberId);
        log.info("adfsfafsf={}", c);
        return c;
    }

    // 활동 페이지
    // 내가 올린 영상에 좋아요를 누른 멤버들을 가져옴
    @GetMapping("/my_video/{memberId}")
    public List<Temp> getLikesOnMyVideo(@PathVariable String memberId) {
        Member findMember = memberRepository.findById(Long.valueOf(memberId)).orElseThrow();
        List<Likes> likes = likesJpaRepository.findLikesOnMyVideo(memberId);
        List<Temp> dtoList = new ArrayList<>();


        for (Likes like : likes) {
            dtoList.add(new Temp(like.getCreatedAt(), like.getMember()));
        }

        return dtoList;
    }


    @DeleteMapping
    public void deleteLikes(@RequestParam Long userId, @RequestParam Long videoId) {
        likesService.removeVideoLikes(userId, videoId);
    }

    @DeleteMapping("/comment")
    public void deleteCommentLikes(@RequestParam Long userId, @RequestParam Long commentId) {
        likesService.removeCommentLikes(userId, commentId);
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
}

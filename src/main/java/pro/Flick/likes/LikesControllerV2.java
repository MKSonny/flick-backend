package pro.Flick.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.comment.trash.dto.CommentLikesAddDTO;
import pro.Flick.entity.Likes;
import pro.Flick.entity.Member;
import pro.Flick.entity.NotificationType;
import pro.Flick.likes.dto.LikeOnMyVideoResponseDTO;
import pro.Flick.member.repository.MemberRepository;
import pro.Flick.notification.NotificationService;
import pro.Flick.repsository.LikesJpaRepository;

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
    private final NotificationService notificationService;


    @PostMapping
    public void addLikesV2(@RequestBody LikesRequestDTO likesRequestDTO) {
        likesService.addLikes(likesRequestDTO.getUserId(), likesRequestDTO.getVideoId());

        notificationService.send(likesRequestDTO.getVideoUserId(), Long.valueOf(likesRequestDTO.getUserId()), NotificationType.LIKE, null);
    }

    @PostMapping("/comment")
    public void addCommentLikes(@RequestBody CommentLikesAddDTO requestDto) {
        likesService.addCommentLikesModifyingV2(requestDto.getUserId(), requestDto.getCommentId());
    }

    // 매번 영상을 불러올때마다 스프링에 데이터를 가져오는 것은 매우 비효율적
    // 그래서 아래 코드 작성
    // Likes 테이블에서 특정 member_id를 기준으로 해당 유저가 누른 좋아요들을 조회
    @GetMapping("/{memberId}")
    public List<MemberLikesResponseDTO> getLikesByMemberId(@PathVariable String memberId) {
        List<Likes> likesByMemberId = likesJpaRepository.findLikesByMemberId(memberId);
        List<MemberLikesResponseDTO> GetLikesByMemberIdResponseDTP = new ArrayList<>();

        for (Likes likes : likesByMemberId) {
            GetLikesByMemberIdResponseDTP.add(new MemberLikesResponseDTO(likes.getId(), likes.getMember().getId(), likes.getVideo().getId(), likes.getCreatedAt()));
        }

        return GetLikesByMemberIdResponseDTP;
    }

    // 활동 페이지
    // 내가 올린 영상에 좋아요를 누른 멤버들을 가져옴
    @GetMapping("/my_video/{memberId}")
    public List<LikeOnMyVideoResponseDTO> getLikesOnMyVideo(@PathVariable String memberId) {
        Member findMember = memberRepository.findById(Long.valueOf(memberId)).orElseThrow();
        List<Likes> likes = likesJpaRepository.findLikesOnMyVideo(memberId);
        List<LikeOnMyVideoResponseDTO> dtoList = new ArrayList<>();

        for (Likes like : likes) {
            dtoList.add(new LikeOnMyVideoResponseDTO(like.getCreatedAt(), like.getMember()));
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
}

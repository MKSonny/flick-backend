package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Likes;
import pro.Flick.entity.Member;
import pro.Flick.entity.Video;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.LikesRepository;
import pro.Flick.repsository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    @PostMapping("/likes")
    public void addLikes(@RequestBody LikesRequestDto likesRequestDto) {
        Member findMember = memberRepository.findMemberById(likesRequestDto.getUserId());
        Video findVideo = fileRepository.findVideoById(likesRequestDto.getVideoId());

        likesRepository.addLike(findMember, findVideo);

    }

    // 매번 영상을 불러올때마다 스프링에 데이터를 가져오는 것은 매우 비효율적
    // 그래서 아래 코드 작성
    // Likes 테이블에서 특정 member_id를 기준으로 해당 유저가 누른 좋아요들을 조회
    @GetMapping("/likes/{memberId}")
    public List<GetLikesByMemberIdResponseDto> getLikesByMemberId(@PathVariable String memberId) {
        List<Likes> likesByMemberId = likesRepository.findLikesByMemberId(memberId);
        List<GetLikesByMemberIdResponseDto> GetLikesByMemberIdResponseDto = new ArrayList<>();

        for (Likes likes : likesByMemberId) {
            GetLikesByMemberIdResponseDto.add(new GetLikesByMemberIdResponseDto(likes.getId(), likes.getMember().getId(), likes.getVideo().getId(), likes.getCreatedAt()));
        }

        return GetLikesByMemberIdResponseDto;
    }

    @DeleteMapping("/likes")
    public void deleteLikes(@RequestParam String userId, @RequestParam String videoId) {
        likesRepository.deleteLikes(userId, videoId);
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

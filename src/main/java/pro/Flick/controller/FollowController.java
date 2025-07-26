package pro.Flick.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.entity.Follower;
import pro.Flick.entity.Member;
import pro.Flick.repsository.FollowRepository;
import pro.Flick.repsository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FollowController {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @PostMapping("/followers")
    public void addFollower(@RequestBody FollowRequestDto requestDto) {
        Member findMember = memberRepository.findMemberById(requestDto.getUserId());
        Member theMemberThatFineMemberWillFollow = memberRepository.findMemberById(requestDto.getFollower_user_id());

        followRepository.memberAFollowsMemberB(findMember, theMemberThatFineMemberWillFollow);
    }

    @GetMapping("/following")
    public List<GetFollowingResultListResponseDto> getFollowing(@RequestParam String userId) {
        List<Follower> following = followRepository.getFollowingByMemberId(userId);
        List<GetFollowingResultListResponseDto> dto = new ArrayList<>();

        for (Follower follower : following) {
            dto.add(new GetFollowingResultListResponseDto(follower.getFollower().getId(), follower.getCreatedAt(), follower.getId(), follower.getMember().getId()));
        }
        return dto;
    }

    // 채팅: 팔로워 정보를 받고 -> 멤버로 교체?
    // follower_user_id는 내 id 이다
//    @GetMapping("followers_inbox")
//    public void getFollowersV2(@RequestParam String follower_user_id) {
//        Member findMember = memberRepository.findMemberById(follower_user_id);
//        List<Follower> followers = findMember.getFollowers();
//        for (Follower follower : followers) {
//            log.info(follower.get);
//        }
//    }


    @GetMapping("/followers")
    public List<GetFollowingResultListResponseDto> getFollowers(@RequestParam String follower_user_id) {
        List<Follower> followers = followRepository.getFollowers(follower_user_id);
        List<GetFollowingResultListResponseDto> dto = new ArrayList<>();

        for (Follower follower : followers) {
            // 나를 팔로우한 사람들의 정보를 뽑아야함
//            Member whoFollowedMe = memberRepository.findMemberById(String.valueOf(follower.getMember().getId()));
            dto.add(new GetFollowingResultListResponseDto(follower.getFollower().getId(), follower.getCreatedAt(), follower.getId(), follower.getMember().getId()));
        }
        return dto;
    }

    @DeleteMapping("/followers")
    public void deleteFollowing(@RequestParam String userId, @RequestParam String follower_user_id) {
        log.info(followRepository.toString());
        followRepository.deleteFollower(userId, follower_user_id);
    }

    @Data
    static class GetFollowingResultListResponseDto {
        private Long follower_user_id;
        private LocalDateTime createAd;
        private Long id;
        private Long user_id;

        public GetFollowingResultListResponseDto(Long follower_user_id, LocalDateTime createAd, Long id, Long user_id) {
            this.follower_user_id = follower_user_id;
            this.createAd = createAd;
            this.id = id;
            this.user_id = user_id;
        }
    }

    @Data
    static class FollowRequestDto {
        private String userId;
        private String follower_user_id;

        public FollowRequestDto(String userId, String follower_user_id) {
            this.userId = userId;
            this.follower_user_id = follower_user_id;
        }
    }
}

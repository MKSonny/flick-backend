package pro.Flick.follow;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Follower;
import pro.Flick.entity.Member;
import pro.Flick.repsository.FollowJpaRepository;
import pro.Flick.member.MemberJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FollowController {

    private final MemberJpaRepository memberJpaRepository;
    private final FollowJpaRepository followJpaRepository;

//    @PostMapping("/followers")
    public void addFollower(@RequestBody FollowRequestDto requestDto) {
        Member findMember = memberJpaRepository.findMemberById(requestDto.getFollower_user_id());
        Member theMemberThatFineMemberWillFollow = memberJpaRepository.findMemberById(requestDto.getUserId());

        followJpaRepository.memberAFollowsMemberB(findMember, theMemberThatFineMemberWillFollow);
    }

    // 내가 팔로잉하는 사람들의 목록
    @GetMapping("/following")
    public List<GetFollowingResultListResponseDto> getFollowing(@RequestParam String userId) {
        List<Follower> following = followJpaRepository.getFollowingByMemberId(userId);
        List<GetFollowingResultListResponseDto> dto = new ArrayList<>();

        for (Follower follower : following) {
            dto.add(new GetFollowingResultListResponseDto(follower.getFollower().getId(), follower.getCreatedAt(), follower.getId(), follower.getMember().getId()));
        }
//        log.info("dto={}", dto);
        return dto;
    }

    // 채팅: 팔로워 정보를 받고 -> 멤버로 교체?
    // follower_user_id는 내 id 이다
//    @GetMapping("followers_inbox")
//    public void getFollowersV2(@RequestParam String follower_user_id) {
//        Member findMember = memberJpaRepository.findMemberById(follower_user_id);
//        List<Follower> followers = findMember.getFollowers();
//        for (Follower follower : followers) {
//            log.info(follower.get);
//        }
//    }

    @GetMapping("/followers")
    public List<FollowerResponseDto> getFollowersFetch(@RequestParam String follower_user_id) {
        List<Follower> followersFetch = followJpaRepository.getFollowersFetch(follower_user_id);

        return followersFetch.stream().map(f -> {
            Member m = f.getFollower(); // 나를 팔로우한 사람

            return new FollowerResponseDto(
                    f.getId(),
                    f.getMember().getId(),
                    f.getFollower().getId(),
                    f.getCreatedAt(),
                    m
            );
        }).toList();
    }

    @Data
    static class MemberInfoDto {
        Long id;
        String username;
        String email;
        LocalDateTime createdAt;

        public MemberInfoDto(Long id, String username, String email, LocalDateTime createdAt) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.createdAt = createdAt;
        }
    }

    @Data
    static class FollowerResponseDto {
        Long id;
        Long userId;
        Long follower_user_id;
        LocalDateTime createdAt;
        GetMemberByIdResponseDto User;

        public FollowerResponseDto(Long id, Long userId, Long follower_user_id, LocalDateTime createdAt, Member member) {
            this.id = id;
            this.userId = userId;
            this.follower_user_id = follower_user_id;
            this.createdAt = createdAt;
            this.User = new GetMemberByIdResponseDto(member);
        }
    }

//    @GetMapping("/followers")
//    public List<GetFollowingResultListResponseDto> getFollowers(@RequestParam String follower_user_id) {
//        List<Follower> followers = followJpaRepository.getFollowers(follower_user_id);
//        List<GetFollowingResultListResponseDto> dto = new ArrayList<>();
//
//        for (Follower follower : followers) {
//            // 나를 팔로우한 사람들의 정보를 뽑아야함
    ////            Member whoFollowedMe = memberJpaRepository.findMemberById(String.valueOf(follower.getMember().getId()));
//            dto.add(new GetFollowingResultListResponseDto(follower.getFollower().getId(), follower.getCreatedAt(), follower.getId(), follower.getMember().getId()));
//        }
//        return dto;
//    }

//    @DeleteMapping("/followers")
    public void deleteFollowing(@RequestParam String userId, @RequestParam String follower_user_id) {
        log.info(followJpaRepository.toString());
        followJpaRepository.deleteFollower(userId, follower_user_id);
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

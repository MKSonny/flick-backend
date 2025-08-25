package pro.Flick.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pro.Flick.Video.dto.VideoWithMemberDto;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberControllerV2 {

    private final MemberService memberService;
//    private final MemberJpaRepository memberJpaRepository;


    @GetMapping("/videos/{profileUserId}")
    public Page<VideoWithMemberDto> MCV2getVideosByUserIdUsingPagingV5(
            @PathVariable Long profileUserId,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return memberService.getVideosByMemberIdWithMemberPage(pageable, profileUserId);
    }

    @GetMapping("/profile-info/{userId}")
    public ProfileInfoResponseDTO getProfileInfo(@PathVariable Long userId) {

//        memberService
        return memberService.getMemberInfo(userId);
    }

    @GetMapping("/profile-info/user/{profileId}/viewer/{myId}")
    public ProfileInfoResponseDTOV2 MCV2getProfileInfo(@PathVariable Long profileId, @PathVariable Long myId) {

        return memberService.getMemberInfoV2(myId, profileId);
    }


    @GetMapping("/got-likes/{memberId}")
    public Long MCV2getRespondLikesByMemberId(@PathVariable Long memberId) {
        return memberService.getLikesCountByMemberId(memberId);
    }

    @GetMapping("/{userId}/followers")
    public Page<FollowerInfoDTOV2> MCV2getMyFollowers(@PathVariable Long userId, @PageableDefault(size = 20) Pageable pageable) {
//        return memberService.getFollowersByMemberId(pageable, userId);
//        return memberService.getFollowersByMemberIdV2(pageable, userId);
        return memberService.getFollowersByMemberIdV3(pageable, userId);
    }

//    @PostMapping("/auth/signup")
//    public GetMemberByIdResponseDto addMember(@RequestBody SignUpDto signUpDto) {
//        log.info("username={}, email={}, password={}", signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
//        Member member = memberJpaRepository.save(signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
//        return new GetMemberByIdResponseDto(member);
//    }
//
//    @GetMapping("/auth/signin")
//    public GetMemberByIdResponseDto signIn(String email, String password) {
////        log.info("email={}, password={}", signInDto.getEmail(), signInDto.getPassword());
//        Member member = memberJpaRepository.findMember(email, password);
//        if (member != null) {
//            // todo
//        }
//        return new GetMemberByIdResponseDto(member);
//    }
//
//    @GetMapping("/auth/get_member")
//    public GetMemberByIdResponseDto getMemberById(String id) {
//        Member byId = memberJpaRepository.findMemberById(id);
//        return new GetMemberByIdResponseDto(byId);
//    }
//
//    // 중복 역할 해결 필요
//    @GetMapping("/get_member/{user_id}")
//    public GetMemberByIdResponseDto getMemberById2(@PathVariable String user_id) {
//        log.info("user_id={}", user_id);
//        Member byId = memberJpaRepository.findMemberById(user_id);
//        return new GetMemberByIdResponseDto(byId);
//    }
//
//    @GetMapping("/members/search")
//    public List<FindMemberByUsernameResponseDto> findMemberByUsername(@RequestParam String username) {
//        List<Member> members = memberJpaRepository.findMemberByUsername(username);
//        List<FindMemberByUsernameResponseDto> findMemberByUsernameResponseDtos = new ArrayList<>();
//
//        for (Member member : members) {
//            findMemberByUsernameResponseDtos.add(new FindMemberByUsernameResponseDto(member.getCreateTime(), member.getEmail(), member.getId(), member.getUsername()));
//        }
//
//        return findMemberByUsernameResponseDtos;
//    }
//
//    // 7/31
//    // 친구 목록 상세 정보 보내주기
////    여기서 POST로 보낸 이유:
////    배열을 RequestParam으로 보내기 복잡하니까 RequestBody로 처리한다
//    @PostMapping("/members/friends_ids")
//    public List<GetMemberByIdResponseDto> getUsersByIds(@RequestBody List<Long> ids) {
//        List<Member> members = memberJpaRepository.findMemberByIds(ids);
//        List<GetMemberByIdResponseDto> dtoList = new ArrayList<>();
//
//        for (Member member : members) {
//            dtoList.add(new GetMemberByIdResponseDto(member));
//        }
//        return dtoList;
//    }
//
//
//    @Data
//    static class FindMemberByUsernameResponseDto {
//        private LocalDateTime created_at;
//        private String email;
//        private Long id;
//        private String username;
//
//        public FindMemberByUsernameResponseDto(LocalDateTime created_at, String email, Long id, String username) {
//            this.created_at = created_at;
//            this.email = email;
//            this.id = id;
//            this.username = username;
//        }
//    }
}

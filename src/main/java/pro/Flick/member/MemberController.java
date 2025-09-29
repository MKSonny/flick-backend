package pro.Flick.member;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.controller.dto.SignUpDto;
import pro.Flick.member.dto.FindMembersByUsernameResponseDto;
import pro.Flick.member.dto.SignInRequestDto;
import pro.Flick.member.entity.Member;
import pro.Flick.member.repository.MemberRepository;
import pro.Flick.trace.LogTrace;
import pro.Flick.trace.template.TraceTemplate;
import pro.Flick.util.JWTUtil;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final LogTrace logTrace;
    private final TraceTemplate traceTemplate;
    private final MemberRepository memberRepository;

    @GetMapping("/api/me")
    public ResponseEntity<MyInfoResponseDto> getMyInfo(@AuthenticationPrincipal CustomUserDetails user) {
        // 1. SecurityContextHolder에서 현재 사용자의 인증 정보를 가져옴

        // 2. 인증 정보에서 사용자 이름(username)을 추출
        //    (JWT 토큰을 만들 때 넣었던 정보)
        String username = user.getUsername();

        // 3. 사용자 이름을 기반으로 서비스 계층에서 전체 사용자 정보를 조회
        Member member = memberRepository.findByUsername(username);

        // 4. 조회된 사용자 정보를 DTO로 변환하여 반환
        MyInfoResponseDto responseDto = new MyInfoResponseDto(member);

        return ResponseEntity.ok(responseDto);
    }

    @Getter
    public class MyInfoResponseDto {

        private final Long id;
        private final String username;
        private final String profile_image_uri;
        // ... 프론트엔드에 필요한 다른 사용자 정보들

        // Member 엔티티를 받아서 DTO를 생성하는 생성자
        public MyInfoResponseDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.profile_image_uri = member.getProfileImageUri();
        }
    }

    /**
     * @Data
     * public class SignUpDto {
     *     private String username;
     *     private String email;
     *     private String password;
     * }
     * @param signUpDto
     * @return
     */
    @PostMapping("/auth/signup")
    public Long signUp(@RequestBody SignUpDto signUpDto) {

//        Member member = memberJpaRepository.save(signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
//        return new GetMemberByIdResponseDto(member);

//        memberService.signUp(signUpDto.getUsername(), signUpDto.getPassword());
//        return memberService.saveMember(signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
        return memberService.signUpV2(signUpDto);
    }

    @GetMapping("/auth/v2/signin")
    public void signInV2(@RequestBody SignInRequestDto requestDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
//        );

//        String token = jwtUtil.generateToken(authentication, 10000L);

//        memberService.getMemberByEmailAndPassword()
    }

    @GetMapping("/auth/v3/signin")
    public void signInV3(@RequestBody SignInRequestDto requestDto) {

    }

    @GetMapping("/auth/signin")
    public GetMemberByIdResponseDto MCsignIn(String email, String password) {

//        return traceTemplate.execute("MemberController.signIn", () -> {
//            Member member = memberJpaRepository.findMember(email, password);
//
//            return new GetMemberByIdResponseDto(member);
//        });


//        AbstractTemplate<GetMemberByIdResponseDto> abstractTemplate = new AbstractTemplate<>(logTrace) {
//
//            @Override
//            protected GetMemberByIdResponseDto call() {
//                Member member = memberJpaRepository.findMember(email, password);
//
//                return new GetMemberByIdResponseDto(member);
//            }
//        };
//        return abstractTemplate.execute("MemberController.signIn");

        return memberService.getMemberByEmailAndPassword(email, password);
    }

    @GetMapping("/auth/get_member")
    public GetMemberByIdResponseDto MCgetMemberById(Long id) {
//        Member byId = memberJpaRepository.findMemberById(id);
        return memberService.getMemberById(id);
//        return new GetMemberByIdResponseDto(byId);
    }

    // 중복 역할 해결 필요
    @GetMapping("/get_member/{user_id}")
    public GetMemberByIdResponseDto MCgetMemberById2(@PathVariable Long user_id) {
//        Member byId = memberJpaRepository.findMemberById(user_id);

        return memberService.getMemberById(user_id);
//        return new GetMemberByIdResponseDto(byId);
    }

    @GetMapping("/members/search")
    public List<FindMembersByUsernameResponseDto> MCfindMemberByUsername(@RequestParam String username) {
//        List<Member> members = memberJpaRepository.findMemberByUsername(username);
//
//
//        List<FindMemberByUsernameResponseDto> findMemberByUsernameResponseDtos = new ArrayList<>();
//
//        for (Member member : members) {
//            findMemberByUsernameResponseDtos.add(new FindMemberByUsernameResponseDto(member.getCreateTime(), member.getEmail(), member.getId(), member.getUsername()));
//        }
//
//        return findMemberByUsernameResponseDtos;

        return memberService.getMembersByUsername(username);
    }

    // 7/31
    // 친구 목록 상세 정보 보내주기
//    여기서 POST로 보낸 이유:
//    배열을 RequestParam으로 보내기 복잡하니까 RequestBody로 처리한다
    @PostMapping("/members/friends_ids")
    public List<GetMemberByIdResponseDto> MCgetUsersByIds(@RequestBody List<Long> ids) {
//        List<Member> members = memberJpaRepository.findMemberByIds(ids);
//
//        List<GetMemberByIdResponseDto> dtoList = new ArrayList<>();
//
//        for (Member member : members) {
//            dtoList.add(new GetMemberByIdResponseDto(member));
//        }
//        return dtoList;

        return memberService.getMembersByIds(ids);
    }


    @Data
    static class FindMemberByUsernameResponseDto {
        private LocalDateTime created_at;
        private String email;
        private Long id;
        private String username;

        public FindMemberByUsernameResponseDto(LocalDateTime created_at, String email, Long id, String username) {
            this.created_at = created_at;
            this.email = email;
            this.id = id;
            this.username = username;
        }
    }
}

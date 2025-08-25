package pro.Flick.member;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jaad.aac.tools.MS;
import org.springframework.web.bind.annotation.*;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.controller.dto.SignUpDto;
import pro.Flick.entity.Member;
import pro.Flick.trace.LogTrace;
import pro.Flick.trace.template.AbstractTemplate;
import pro.Flick.trace.template.TraceTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

//    private final MemberJpaRepository memberJpaRepository;

    private final MemberService memberService;
    private final LogTrace logTrace;
    private final TraceTemplate traceTemplate;

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
    public GetMemberByIdResponseDto MCaddMember(@RequestBody SignUpDto signUpDto) {

//        Member member = memberJpaRepository.save(signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
//        return new GetMemberByIdResponseDto(member);

        return memberService.saveMember(signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
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

package pro.Flick.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.controller.dto.SignInDto;
import pro.Flick.controller.dto.SignUpDto;
import pro.Flick.entity.Member;
import pro.Flick.repsository.MemberRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        memberRepository.save("username", "Email", "123");
    }

    @PostMapping("/auth/signup")
    public GetMemberByIdResponseDto addMember(@RequestBody SignUpDto signUpDto) {
        log.info("username={}, email={}, password={}", signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
        Member member = memberRepository.save(signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
        return new GetMemberByIdResponseDto(member);
    }

    @GetMapping("/auth/signin")
    public GetMemberByIdResponseDto signIn(String email, String password) {
//        log.info("email={}, password={}", signInDto.getEmail(), signInDto.getPassword());
        Member member = memberRepository.findMember(email, password);
        if (member != null) {
            // todo
        }
        return new GetMemberByIdResponseDto(member);
    }

    @GetMapping("/auth/get_member")
    public GetMemberByIdResponseDto getMemberById(String id) {
        Member byId = memberRepository.findMemberById(id);
        return new GetMemberByIdResponseDto(byId);
    }
}

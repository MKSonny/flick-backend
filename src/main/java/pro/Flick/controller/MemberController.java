package pro.Flick.controller;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.controller.dto.SignUpDto;
import pro.Flick.entity.Member;
import pro.Flick.repsository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        memberRepository.save("HelloWorld", "Email", "123");
        memberRepository.save("test", "Email2", "123");
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

    @GetMapping("/members/search")
    public List<FindMemberByUsernameResponseDto> findMemberByUsername(@RequestParam String username) {
        List<Member> members = memberRepository.findMemberByUsername(username);
        List<FindMemberByUsernameResponseDto> findMemberByUsernameResponseDtos = new ArrayList<>();

        for (Member member : members) {
            findMemberByUsernameResponseDtos.add(new FindMemberByUsernameResponseDto(member.getCreateTime(), member.getEmail(), member.getId(), member.getUsername()));
        }

        return findMemberByUsernameResponseDtos;
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

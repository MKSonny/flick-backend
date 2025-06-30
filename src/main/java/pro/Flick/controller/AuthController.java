package pro.Flick.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.controller.dto.SignUpDto;
import pro.Flick.repsository.MemberRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberRepository memberRepository;

    @PostMapping("/auth/signup")
    public void addMember(@RequestBody SignUpDto signUpDto) {
        log.info("username={}, email={}, password={}", signUpDto.getUsername(), signUpDto.getEmail(), signUpDto.getPassword());
        memberRepository.save(signUpDto.getEmail());
    }
}

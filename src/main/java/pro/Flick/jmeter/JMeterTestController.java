package pro.Flick.jmeter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.Flick.controller.dto.GetMemberByIdResponseDto;
import pro.Flick.entity.Member;
import pro.Flick.repsository.MemberRepository;

import java.util.List;

@RestController
@RequestMapping("/jmeter")
@RequiredArgsConstructor
public class JMeterTestController {

    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public List<GetMemberByIdResponseDto> getMembers() {
        List<Member> all = memberRepository.findAll();
        return all.stream().map(GetMemberByIdResponseDto::new).toList();
    }
}

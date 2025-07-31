package pro.Flick.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.Flick.entity.Member;
import pro.Flick.repsository.FileRepository;
import pro.Flick.repsository.MemberRepository;
import pro.Flick.service.FollowService;

@Service
@RequiredArgsConstructor
public class DbInit {
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final FollowService followService;

    @Transactional
    public void saveMemberAndVideo() {
        memberRepository.save("HelloWorld", "Email", "123");
        memberRepository.save("test", "Email2", "123");


        Member member = memberRepository.findMember("Email", "123");
        Member member2 = memberRepository.findMember("Email2", "123");
        fileRepository.saveVideo("myVideo", "http://127.0.0.1:8080/video/test.mov", member);
        fileRepository.saveVideo("myVideo2", "http://127.0.0.1:8080/video/test2.mov", member);
        fileRepository.saveVideo("myVideo3", "http://127.0.0.1:8080/video/test3.mov", member2);

        followService.memberAFollowsMemberB(member, member2);
        followService.memberAFollowsMemberB(member2, member);
    }
}

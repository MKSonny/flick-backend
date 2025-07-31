package pro.Flick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.entity.Member;
import pro.Flick.repsository.FollowRepository;
import pro.Flick.repsository.MemberRepository;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public void memberAFollowsMemberB(String A, String B) {
        Member memberA = memberRepository.findMemberById(A);
        Member memberB = memberRepository.findMemberById(B);
        followRepository.memberAFollowsMemberB(memberA, memberB);
    }

    public void memberAFollowsMemberB(Member memberA, Member memberB) {
        followRepository.memberAFollowsMemberB(memberA, memberB);
    }
}

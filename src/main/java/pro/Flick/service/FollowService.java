package pro.Flick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.entity.Member;
import pro.Flick.repsository.FollowRepository;
import pro.Flick.repsository.MemberJpaRepository;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final MemberJpaRepository memberJpaRepository;
    private final FollowRepository followRepository;

    public void memberAFollowsMemberB(String A, String B) {
        Member memberA = memberJpaRepository.findMemberById(A);
        Member memberB = memberJpaRepository.findMemberById(B);
        followRepository.memberAFollowsMemberB(memberA, memberB);
    }

    public void memberAFollowsMemberB(Member memberA, Member memberB) {
        followRepository.memberAFollowsMemberB(memberA, memberB);
    }
}

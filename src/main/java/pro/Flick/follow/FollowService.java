package pro.Flick.follow;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.entity.Follower;
import pro.Flick.entity.Member;
import pro.Flick.repsository.FollowJpaRepository;
import pro.Flick.repsository.FollowRepository;
import pro.Flick.member.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowJpaRepository followJpaRepository;

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public void memberAFollowsMemberB(String A, String B) {
        Member memberA = memberRepository.findMemberById(A);
        Member memberB = memberRepository.findMemberById(B);
        followJpaRepository.memberAFollowsMemberB(memberA, memberB);
    }

    public void memberAFollowsMemberB(Member memberA, Member memberB) {
        followJpaRepository.memberAFollowsMemberB(memberA, memberB);
    }


    @Transactional
    public void memberAFollowsMemberBUsingRef(Long memberAId, Long memberBId) {

        Member memberARef = memberRepository.getReferenceById(memberAId);
        Member memberBRef = memberRepository.getReferenceById(memberBId);


        followRepository.save(new Follower(memberARef, memberBRef, LocalDateTime.now()));

    }

    @Transactional
    public void deleteFollower(Long followerId, Long memberId) {
        followRepository.deleteFollowByFollowerIdAndMemberId(followerId, memberId);
    }
}

package pro.Flick.follow;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.Flick.entity.Follower;
import pro.Flick.entity.Member;
import pro.Flick.follow.dto.FollowResponseDto;
import pro.Flick.follow.repository.FollowerJpaRepository;
import pro.Flick.follow.repository.FollowerRepository;
import pro.Flick.member.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowerJpaRepository followJpaRepository;

    private final FollowerRepository followRepository;
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
    public FollowResponseDto memberAFollowsMemberBUsingRef(Long memberAId, Long memberBId) {

        Member memberARef = memberRepository.getReferenceById(memberAId);
        Member memberBRef = memberRepository.getReferenceById(memberBId);


        Follower saved = followRepository.save(new Follower(memberARef, memberBRef, LocalDateTime.now()));

        return FollowResponseDto.builder()
                .id(saved.getId())
                .followerId(memberAId)
                .followingId(memberBId)
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteFollower(Long myId, Long memberId) {
        followRepository.deleteFollowByFollowerIdAndMemberId(myId, memberId);
    }

    public Boolean isAMemberIdFollowingBMemberId(Long AMemberId, Long BMemberId) {
        return followRepository.QfindAmIFollowing(AMemberId, BMemberId);
    }
}
